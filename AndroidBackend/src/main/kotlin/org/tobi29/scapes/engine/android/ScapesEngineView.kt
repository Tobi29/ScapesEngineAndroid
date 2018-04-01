/*
 * Copyright 2012-2018 Tobi29
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
import org.tobi29.logging.KLogging
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.android.input.AndroidControllerDesktop
import org.tobi29.scapes.engine.android.input.AndroidControllerJoystick
import org.tobi29.scapes.engine.android.input.AndroidControllerTouch
import org.tobi29.scapes.engine.input.Controller
import org.tobi29.scapes.engine.input.ControllerButtons
import org.tobi29.scapes.engine.input.ControllerJoystick
import org.tobi29.stdex.math.floorToInt
import org.tobi29.utils.EventDispatcher

class ScapesEngineView(
    context: Context,
    val engine: ScapesEngine,
    attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs) {
    val containerWidth get() = (width / densityX).floorToInt()
    val containerHeight get() = (height / densityY).floorToInt()
    var densityX = 1.0
    var densityY = 1.0
    private val inputManager = context.getSystemService(
        Context.INPUT_SERVICE
    ) as InputManager
    private val devices = SparseArray<Controller>()
    private val touchController = AndroidControllerTouch()
    private val defaultController = AndroidControllerDesktop()
    private var defaultDevices = 0
    private val deviceListener = object : InputManager.InputDeviceListener {
        override fun onInputDeviceRemoved(deviceId: Int) {
            val controller = devices.get(deviceId) ?: return
            devices.remove(deviceId)
            if (controller == defaultController) {
                defaultDevices--
                assert(defaultDevices >= 0)
                if (defaultDevices == 0) {
                    engine.events.fire(Controller.RemoveEvent(controller))
                }
            }
        }

        override fun onInputDeviceAdded(deviceId: Int) {
            val device = inputManager.getInputDevice(deviceId)
            if(device.isVirtual) return
            if (device.isFullKeyboard
                || device.isType(InputDevice.SOURCE_MOUSE)) {
                if (device.isFullKeyboard)
                    logger.info { "Detected keyboard: ${device.name}" }
                if (device.isType(InputDevice.SOURCE_MOUSE))
                    logger.info { "Detected mouse: ${device.name}" }

                if (defaultDevices == 0)
                    engine.events.fire(Controller.AddEvent(defaultController))
                defaultDevices++
                devices.put(deviceId, defaultController)
            }
            if (device.isType(
                    InputDevice.SOURCE_GAMEPAD or
                            InputDevice.SOURCE_JOYSTICK
                )) {
                logger.info { "Detected gamepad: ${device.name}" }
                val controller = AndroidControllerJoystick(
                    device.name,
                    ControllerJoystick.Type.GAMEPAD,
                    device.motionRanges.size
                )
                engine.events.fire(Controller.AddEvent(controller))
                devices.put(deviceId, controller)
            }
        }

        override fun onInputDeviceChanged(deviceId: Int) {
            onInputDeviceRemoved(deviceId)
            onInputDeviceAdded(deviceId)
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
        engine.events.fire(Controller.AddEvent(touchController))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        val device = event.device ?: return true
        if (device.isType(InputDevice.SOURCE_TOUCHSCREEN)) {
            touchController.handle(event, densityX, densityY)
        } else {
            val controller = devices[event.deviceId]
            when (controller) {
                is AndroidControllerDesktop -> {
                    if (device.isType(InputDevice.SOURCE_MOUSE)) {
                        handleMousePointer(
                            event, densityX, densityY,
                            controller, engine.events
                        )
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
            is AndroidControllerJoystick -> {
                device.motionRanges.withIndex().forEach { (i, motionRange) ->
                    controller.setAxis(
                        i,
                        deadzones(
                            event.getAxisValue(motionRange.axis).toDouble()
                        ),
                        engine.events
                    )
                }
            }
            is AndroidControllerDesktop -> {
                if (device.isType(InputDevice.SOURCE_MOUSE)) {
                    when (event.action) {
                        MotionEvent.ACTION_BUTTON_PRESS -> {
                            AndroidKeyMap.button(event.actionButton)?.let {
                                controller.addPressEvent(
                                    it, ControllerButtons.Action.PRESS,
                                    engine.events
                                )
                            }
                        }
                        MotionEvent.ACTION_BUTTON_RELEASE -> {
                            AndroidKeyMap.button(event.actionButton)?.let {
                                controller.addPressEvent(
                                    it, ControllerButtons.Action.RELEASE,
                                    engine.events
                                )
                            }
                        }
                    }
                    handleMousePointer(
                        event, densityX, densityY, controller, engine.events
                    )
                }
            }
        }
        return true
    }

    override fun onKeyDown(
        keyCode: Int,
        event: KeyEvent
    ): Boolean {
        super.onKeyDown(keyCode, event)
        AndroidKeyMap.key(keyCode)?.let { key ->
            val controller = devices[event.deviceId]
            when (controller) {
                is AndroidControllerDesktop -> controller.addPressEvent(
                    key, ControllerButtons.Action.PRESS, engine.events
                )
                is AndroidControllerJoystick -> controller.addPressEvent(
                    key, ControllerButtons.Action.PRESS, engine.events
                )
            }
        }
        return true
    }

    override fun onKeyUp(
        keyCode: Int,
        event: KeyEvent
    ): Boolean {
        super.onKeyUp(keyCode, event)
        AndroidKeyMap.key(keyCode)?.let { key ->
            val controller = devices[event.deviceId]
            when (controller) {
                is AndroidControllerDesktop -> controller.addPressEvent(
                    key, ControllerButtons.Action.RELEASE, engine.events
                )
                is AndroidControllerJoystick -> controller.addPressEvent(
                    key, ControllerButtons.Action.RELEASE, engine.events
                )
            }
        }
        return true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        display?.displayMetrics?.let { displayMetrics ->
            densityX = displayMetrics.xdpi.toDouble() / 145.0
            densityY = displayMetrics.ydpi.toDouble() / 145.0
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        densityX = 1.0
        densityY = 1.0
    }

    fun dispose() {
        inputManager.unregisterInputDeviceListener(deviceListener)
    }

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

        private fun handleMousePointer(
            event: MotionEvent,
            densityX: Double,
            densityY: Double,
            controller: AndroidControllerDesktop,
            events: EventDispatcher
        ) {
            handleMousePointer(event, { x, y ->
                controller.set(x / densityX, y / densityY)
            }, { x, y ->
                controller.addDelta(x / densityX, y / densityY, events)
            })
        }

        private inline fun handleMousePointer(
            event: MotionEvent,
            absolute: (Double, Double) -> Unit,
            relative: (Double, Double) -> Unit
        ) {
            var ox = event.x
            var oy = event.y
            absolute(ox.toDouble(), oy.toDouble())
            var dx = 0.0f
            var dy = 0.0f
            for (i in 0 until event.historySize) {
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
