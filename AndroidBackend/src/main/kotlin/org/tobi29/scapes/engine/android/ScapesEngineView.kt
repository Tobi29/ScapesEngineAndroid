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

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.hardware.input.InputManager
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.SparseArray
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import org.tobi29.logging.KLogging
import org.tobi29.scapes.engine.Container
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.android.input.AndroidControllerDesktop
import org.tobi29.scapes.engine.android.input.AndroidControllerJoystick
import org.tobi29.scapes.engine.android.input.AndroidControllerTouch
import org.tobi29.scapes.engine.gui.GuiController
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
    private var cursorCaptured = false
    private val events = EventDispatcher(engine.events) {
        listen<CaptureCursorEvent> { event ->
            post {
                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    cursorCaptured = event.value
                    if (cursorCaptured) requestPointerCapture()
                    else releasePointerCapture()
                }
            }
        }
        listen<MessageEvent> { event ->
            post {
                AlertDialog.Builder(context)
                    .setTitle(event.title)
                    .setMessage(event.message)
                    .setPositiveButton("OK") { _, _ -> }
                    .create().show()
            }
        }
        listen<DialogEvent> { event ->
            post {
                val editText = EditText(context)
                editText.setText(event.text.text.toString())
                val dialog = AlertDialog.Builder(context)
                    .setTitle(event.title)
                    .setView(editText)
                    .setPositiveButton("Done") { _, _ ->
                        if (event.text.text.isNotEmpty()) {
                            event.text.text.clear()
                        }
                        event.text.text.append(editText.text)
                        event.text.cursor = event.text.text.length
                        event.text.dirty.set(true)
                    }
                    .create()
                dialog.show()
                editText.requestFocus()
                val imm = editText.context.getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
            }
        }
    }
    private val deviceListener = object : InputManager.InputDeviceListener {
        override fun onInputDeviceRemoved(deviceId: Int) {
            val controller = devices.get(deviceId) ?: return
            devices.remove(deviceId)
            if (controller == defaultController) {
                defaultDevices--
                assert(defaultDevices >= 0)
                if (defaultDevices == 0) {
                    events.fire(Controller.RemoveEvent(controller))
                }
            }
        }

        override fun onInputDeviceAdded(deviceId: Int) {
            val device = inputManager.getInputDevice(deviceId)
            if (device.isVirtual) return
            if (device.isFullKeyboard
                || device.isType(InputDevice.SOURCE_MOUSE)
                || device.isType(InputDevice.SOURCE_MOUSE_RELATIVE)) {
                if (device.isFullKeyboard)
                    logger.info { "Detected keyboard: ${device.name}" }
                if (device.isType(InputDevice.SOURCE_MOUSE)
                    || device.isType(InputDevice.SOURCE_MOUSE_RELATIVE))
                    logger.info { "Detected mouse: ${device.name}" }

                if (defaultDevices == 0)
                    events.fire(Controller.AddEvent(defaultController))
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
                events.fire(Controller.AddEvent(controller))
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
        events.enable()
        inputManager.registerInputDeviceListener(deviceListener, null)
        inputManager.inputDeviceIds.forEach {
            deviceListener.onInputDeviceAdded(it)
        }
        events.fire(Controller.AddEvent(touchController))
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
                            event, densityX, densityY, controller, events
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
        val device = event.device ?: return false
        when (controller) {
            is AndroidControllerJoystick -> {
                device.motionRanges.withIndex().forEach { (i, motionRange) ->
                    controller.setAxis(
                        i,
                        deadzones(
                            event.getAxisValue(motionRange.axis).toDouble()
                        ),
                        events
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
                                    events
                                )
                            }
                        }
                        MotionEvent.ACTION_BUTTON_RELEASE -> {
                            AndroidKeyMap.button(event.actionButton)?.let {
                                controller.addPressEvent(
                                    it, ControllerButtons.Action.RELEASE,
                                    events
                                )
                            }
                        }
                    }
                    handleMousePointer(
                        event, densityX, densityY, controller, events
                    )
                } else if (device.isType(InputDevice.SOURCE_MOUSE_RELATIVE)) {
                    handleMouseScroll(
                        event, densityX, densityY, controller, events
                    )
                }
            }
        }
        return true
    }

    override fun onCapturedPointerEvent(event: MotionEvent): Boolean {
        super.onCapturedPointerEvent(event)
        val controller = devices[event.deviceId]
        val device = event.device ?: return false
        when (controller) {
            is AndroidControllerDesktop -> {
                if (device.isType(InputDevice.SOURCE_MOUSE_RELATIVE)) {
                    when (event.action) {
                        MotionEvent.ACTION_BUTTON_PRESS -> {
                            AndroidKeyMap.button(event.actionButton)?.let {
                                controller.addPressEvent(
                                    it, ControllerButtons.Action.PRESS,
                                    events
                                )
                            }
                        }
                        MotionEvent.ACTION_BUTTON_RELEASE -> {
                            AndroidKeyMap.button(event.actionButton)?.let {
                                controller.addPressEvent(
                                    it, ControllerButtons.Action.RELEASE,
                                    events
                                )
                            }
                        }
                    }
                    handleMousePointerRelative(
                        event, densityX, densityY, controller, events
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
                    key, ControllerButtons.Action.PRESS, events
                )
                is AndroidControllerJoystick -> controller.addPressEvent(
                    key, ControllerButtons.Action.PRESS, events
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
                    key, ControllerButtons.Action.RELEASE, events
                )
                is AndroidControllerJoystick -> controller.addPressEvent(
                    key, ControllerButtons.Action.RELEASE, events
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

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (android.os.Build.VERSION.SDK_INT >= 26 && hasWindowFocus) {
            if (cursorCaptured) requestPointerCapture()
            else releasePointerCapture()
        }
    }

    override fun onFocusChanged(
        gainFocus: Boolean,
        direction: Int,
        previouslyFocusedRect: Rect?
    ) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        if (android.os.Build.VERSION.SDK_INT >= 26 && gainFocus) {
            if (cursorCaptured) requestPointerCapture()
            else releasePointerCapture()
        }
    }

    fun dispose() {
        inputManager.unregisterInputDeviceListener(deviceListener)
        events.disable()
    }

    companion object : KLogging()
}

class CaptureCursorEvent(
    val value: Boolean
)

class MessageEvent(
    val messageType: Container.MessageType,
    val title: String,
    val message: String
)

class DialogEvent(
    val title: String,
    val text: GuiController.TextFieldData,
    val multiline: Boolean
)

private const val DEADZONES = 0.05
private const val DEADZONES_SCALE = 0.95

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

private fun handleMousePointerRelative(
    event: MotionEvent,
    densityX: Double,
    densityY: Double,
    controller: AndroidControllerDesktop,
    events: EventDispatcher
) {
    handleMouseRelative(event, { x, y ->
        controller.addDelta(x / densityX, y / densityY, events)
    })
}

private fun handleMouseScroll(
    event: MotionEvent,
    densityX: Double,
    densityY: Double,
    controller: AndroidControllerDesktop,
    events: EventDispatcher
) {
    handleMouseRelative(event, { x, y ->
        controller.addScroll(x / densityX, y / densityY, 0, events)
    })
}

private inline fun handleMouseRelative(
    event: MotionEvent,
    relative: (Double, Double) -> Unit
) {
    var dx = 0.0f
    var dy = 0.0f
    for (i in 0 until event.historySize) {
        dx += event.getHistoricalX(i)
        dy += event.getHistoricalY(i)
    }
    relative(dx.toDouble(), dy.toDouble())
}
