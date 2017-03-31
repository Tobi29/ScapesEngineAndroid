/*
 * Copyright 2012-2017 Tobi29
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tobi29.scapes.engine.android

import android.content.Context
import android.hardware.input.InputManager
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.SparseArray
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import org.tobi29.scapes.engine.input.*
import org.tobi29.scapes.engine.utils.EventDispatcher
import org.tobi29.scapes.engine.utils.math.floor
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class ScapesEngineView(
        context: Context,
        attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs), ControllerTouch {
    override val events = EventDispatcher()
    val inputManager = context.getSystemService(
            Context.INPUT_SERVICE) as InputManager
    val fingers = ConcurrentHashMap<Int, ControllerTouch.Tracker>()
    val deviceEvents = ConcurrentLinkedQueue<Any>()
    val containerWidth get() = floor(width / density)
    val containerHeight get() = floor(height / density)
    // TODO: Implement proper density support
    val density get() = 3.0
    private val devices = SparseArray<Controller>()
    private val defaultController = object : ControllerDefault() {
        override val isModifierDown get() =
        isDown(ControllerKey.KEY_CONTROL_LEFT) ||
                isDown(ControllerKey.KEY_CONTROL_RIGHT)
    }
    private var defaultDevices = 0
    private val deviceListener = object : InputManager.InputDeviceListener {
        override fun onInputDeviceRemoved(deviceId: Int) {
            val controller = devices.get(deviceId) ?: return
            devices.remove(deviceId)
            if (controller == defaultController) {
                defaultDevices--
                assert(defaultDevices >= 0)
                if (defaultDevices == 0) {
                    deviceEvents.add(ControllerRemoveEvent(controller))
                }
            }
        }

        override fun onInputDeviceAdded(deviceId: Int) {
            val device = inputManager.getInputDevice(deviceId)
            val sources = device.sources
            if (sources and InputDevice.SOURCE_KEYBOARD != 0 ||
                    sources and InputDevice.SOURCE_MOUSE != 0) {
                if (defaultDevices == 0) {
                    deviceEvents.add(ControllerAddEvent(defaultController))
                }
                defaultDevices++
                devices.put(deviceId, defaultController)
            }
            if (sources and InputDevice.SOURCE_GAMEPAD != 0 &&
                    sources and InputDevice.SOURCE_JOYSTICK != 0) {
                val controller = ControllerJoystick(device.name,
                        device.motionRanges.size)
                deviceEvents.add(ControllerAddEvent(controller))
                devices.put(deviceId, controller)
            }
        }

        override fun onInputDeviceChanged(deviceId: Int) {
            onInputDeviceRemoved(deviceId)
        }
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        setEGLContextClientVersion(3)
        inputManager.registerInputDeviceListener(deviceListener, handler)
        inputManager.inputDeviceIds.forEach {
            deviceListener.onInputDeviceAdded(it)
        }
        deviceEvents.add(ControllerAddEvent(this))
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        super.onTouchEvent(e)
        val density = density
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val index = e.actionIndex
                val tracker = ControllerTouch.Tracker()
                tracker.pos.set(e.getX(index) / density,
                        e.getY(index) / density)
                val id = e.getPointerId(index)
                fingers.put(id, tracker)
            }
            MotionEvent.ACTION_MOVE -> for ((key, tracker) in fingers) {
                val index = e.findPointerIndex(key)
                tracker.pos.set(e.getX(index) / density,
                        e.getY(index) / density)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                val id = e.getPointerId(e.actionIndex)
                fingers.remove(id)
            }
        }
        return true
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        super.onGenericMotionEvent(event)
        val controller = devices[event.deviceId]
        when (controller) {
            is ControllerJoystick -> {
                event.device.motionRanges.withIndex().forEach { (i, motionRange) ->
                    controller.setAxis(i,
                            deadzones(event.getAxisValue(
                                    motionRange.axis).toDouble()))
                }
            }
            is ControllerDefault -> {
                if (event.device.sources and InputDevice.SOURCE_MOUSE != 0) {
                    val x = event.x
                    val y = event.y
                }
            }
        }
        return true
    }

    override fun onKeyDown(keyCode: Int,
                           event: KeyEvent): Boolean {
        super.onKeyDown(keyCode, event)
        val controller = devices[event.deviceId]
        when (controller) {
            is ControllerBasic -> {
                AndroidKeyMap.key(keyCode)?.let {
                    println("$keyCode -> $it")
                    controller.addPressEvent(it,
                            ControllerBasic.PressState.PRESS)
                }
            }
        }
        return true

    }

    override fun onKeyUp(keyCode: Int,
                         event: KeyEvent): Boolean {
        super.onKeyUp(keyCode, event)
        val controller = devices[event.deviceId]
        when (controller) {
            is ControllerBasic -> {
                AndroidKeyMap.key(keyCode)?.let {
                    controller.addPressEvent(it,
                            ControllerBasic.PressState.RELEASE)
                }
            }
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        inputManager.unregisterInputDeviceListener(deviceListener)
    }

    override fun onResume() {
        super.onResume()
        inputManager.registerInputDeviceListener(deviceListener, handler)
    }

    override fun fingers() = fingers.values.asSequence()

    override fun poll() {}

    companion object {
        private val DEADZONES = 0.05
        private val DEADZONES_SCALE = 0.95

        private fun deadzones(value: Double): Double {
            if (value > DEADZONES) {
                return (value - DEADZONES) / DEADZONES_SCALE
            } else if (value < -DEADZONES) {
                return (value + DEADZONES) / DEADZONES_SCALE
            }
            return 0.0
        }
    }
}
