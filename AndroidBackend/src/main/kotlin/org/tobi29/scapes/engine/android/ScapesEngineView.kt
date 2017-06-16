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
import org.tobi29.scapes.engine.utils.logging.KLogging
import org.tobi29.scapes.engine.utils.math.floor
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class ScapesEngineView(
        context: Context,
        attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs), ControllerTouch {
    val fingers = ConcurrentHashMap<Int, ControllerTouch.Tracker>()
    val deviceEvents = ConcurrentLinkedQueue<Any>()
    val containerWidth get() = floor(width / densityX)
    val containerHeight get() = floor(height / densityY)
    val densityX get() =
    (display?.displayMetrics?.xdpi?.toDouble() ?: 440.0) / 145.0
    val densityY get() =
    (display?.displayMetrics?.ydpi?.toDouble() ?: 440.0) / 145.0
    private val inputManager = context.getSystemService(
            Context.INPUT_SERVICE) as InputManager
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
            if (device.isType(InputDevice.SOURCE_KEYBOARD) ||
                    device.isType(InputDevice.SOURCE_MOUSE)) {
                if (device.isType(InputDevice.SOURCE_KEYBOARD)) {
                    logger.info { "Detected keyboard: ${device.name}" }
                }
                if (device.isType(InputDevice.SOURCE_MOUSE)) {
                    logger.info { "Detected mouse: ${device.name}" }
                }
                if (defaultDevices == 0) {
                    deviceEvents.add(ControllerAddEvent(defaultController))
                }
                defaultDevices++
                devices.put(deviceId, defaultController)
            }
            if (device.isType(InputDevice.SOURCE_GAMEPAD or
                    InputDevice.SOURCE_JOYSTICK)) {
                logger.info { "Detected gamepad: ${device.name}" }
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        val device = event.device ?: return true
        if (device.isType(InputDevice.SOURCE_TOUCHSCREEN)) {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                    val index = event.actionIndex
                    val tracker = ControllerTouch.Tracker()
                    tracker.pos.set(event.getX(index) / densityX,
                            event.getY(index) / densityY)
                    val id = event.getPointerId(index)
                    fingers.put(id, tracker)
                }
                MotionEvent.ACTION_MOVE -> for ((key, tracker) in fingers) {
                    val index = event.findPointerIndex(key)
                    tracker.pos.set(event.getX(index) / densityX,
                            event.getY(index) / densityY)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                    val id = event.getPointerId(event.actionIndex)
                    fingers.remove(id)
                }
            }
        } else {
            val controller = devices[event.deviceId]
            when (controller) {
                is ControllerDefault -> {
                    if (device.isType(InputDevice.SOURCE_MOUSE)) {
                        handleMousePointer(event, densityX, densityY,
                                controller)
                    }
                }
            }
        }
        return true
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        super.onGenericMotionEvent(event)
        val controller = devices[event.deviceId]
        val device = event.device ?: return true
        when (controller) {
            is ControllerJoystick -> {
                device.motionRanges.withIndex().forEach { (i, motionRange) ->
                    controller.setAxis(i,
                            deadzones(event.getAxisValue(
                                    motionRange.axis).toDouble()))
                }
            }
            is ControllerDefault -> {
                if (device.isType(InputDevice.SOURCE_MOUSE)) {
                    when (event.action) {
                        MotionEvent.ACTION_BUTTON_PRESS -> {
                            AndroidKeyMap.button(event.actionButton)?.let {
                                controller.addPressEvent(it,
                                        ControllerBasic.PressState.PRESS)
                            }
                        }
                        MotionEvent.ACTION_BUTTON_RELEASE -> {
                            AndroidKeyMap.button(event.actionButton)?.let {
                                controller.addPressEvent(it,
                                        ControllerBasic.PressState.RELEASE)
                            }
                        }
                    }
                    handleMousePointer(event, densityX, densityY, controller)
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

    fun dispose() {
        inputManager.unregisterInputDeviceListener(deviceListener)
    }

    override fun fingers() = fingers.values.asSequence()

    override fun poll(events: EventDispatcher) {}

    companion object : KLogging() {
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

        private fun handleMousePointer(event: MotionEvent,
                                       densityX: Double,
                                       densityY: Double,
                                       controller: ControllerDefault) {
            handleMousePointer(event, { x, y ->
                controller.set(x / densityX, y / densityY)
            }, { x, y ->
                controller.addDelta(x / densityX, y / densityY)
            })
        }

        private inline fun handleMousePointer(event: MotionEvent,
                                              absolute: (Double, Double) -> Unit,
                                              relative: (Double, Double) -> Unit) {
            var ox = event.x
            var oy = event.y
            absolute(ox.toDouble(), oy.toDouble())
            var dx = 0.0f
            var dy = 0.0f
            for (i in 0..event.historySize - 1) {
                val hx = event.getHistoricalX(i)
                val hy = event.getHistoricalY(i)
                dx += ox - hx
                dy += oy - hy
                ox = hx
                oy = hy
            }
            relative(dx.toDouble(), dy.toDouble())
        }
    }
}
