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
import org.tobi29.coroutines.Timer
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
import org.tobi29.stdex.atomic.AtomicBoolean
import org.tobi29.stdex.atomic.AtomicReference
import org.tobi29.stdex.math.floorToInt
import org.tobi29.utils.EventDispatcher
import org.tobi29.utils.sleep
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ScapesEngineView(
    context: Context,
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
    private val engine get() = attached.get()?.first
    private val container get() = attached.get()?.second
    private val events get() = attached.get()?.third
    private val attached =
        AtomicReference<Triple<ScapesEngine, AndroidContainer, EventDispatcher>?>()
    private val renderThread = AtomicReference<Thread?>(null)
    private val reset = AtomicBoolean(false)
    private val deviceListener = object : InputManager.InputDeviceListener {
        override fun onInputDeviceRemoved(deviceId: Int) {
            val controller = devices.get(deviceId) ?: return
            devices.remove(deviceId)
            if (controller == defaultController) {
                defaultDevices--
                assert(defaultDevices >= 0)
                if (defaultDevices == 0) {
                    events?.fire(Controller.RemoveEvent(controller))
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
                    events?.fire(Controller.AddEvent(defaultController))
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
                events?.fire(Controller.AddEvent(controller))
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
        setRenderer(object : Renderer {
            private val timer = Timer()
            private var widthResolution = 0
            private var heightResolution = 0

            override fun onSurfaceCreated(
                gl: GL10,
                config: EGLConfig
            ) {
                resetGL()
                timer.init()
            }

            override fun onSurfaceChanged(
                gl: GL10,
                width: Int,
                height: Int
            ) {
                widthResolution = width
                heightResolution = height
            }

            override fun onDrawFrame(gl: GL10) {
                val tickDiff = timer.tick()
                val delta = Timer.toDelta(tickDiff).coerceIn(0.0001, 0.1)
                render(delta, widthResolution, heightResolution)
            }
        })
    }

    fun attach(engine: ScapesEngine, container: AndroidContainer) {
        val new = Triple(engine, container, EventDispatcher(engine.events) {
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
        })
        if (!attached.compareAndSet(null, new))
            throw IllegalStateException("An engine is already attached")

        new.third.enable()
        inputManager.registerInputDeviceListener(deviceListener, null)
        inputManager.inputDeviceIds.forEach {
            deviceListener.onInputDeviceAdded(it)
        }
        new.third.fire(Controller.AddEvent(touchController))
    }

    fun detach(engine: ScapesEngine, container: AndroidContainer) {
        val old = attached.getAndSet(null)
        if (old?.first !== engine || old.second !== container)
            throw IllegalStateException("No or a different engine is attached")

        inputManager.inputDeviceIds.forEach {
            deviceListener.onInputDeviceRemoved(it)
        }
        inputManager.unregisterInputDeviceListener(deviceListener)
        old.third.disable()
    }

    private fun render(
        delta: Double,
        width: Int,
        height: Int
    ) {
        val currentThread = Thread.currentThread()
        if (!renderThread.compareAndSet(null, currentThread))
            error("Rendering twice at the same time")
        val engine = engine ?: return
        val container = container ?: return
        if (reset.getAndSet(false)) engine.graphics.reset()
        try {
            while (!engine.graphics.render(
                    container.gl, delta, width, height,
                    containerWidth, containerHeight
                )) {
                sleep(1L)
            }
        } finally {
            if (!renderThread.compareAndSet(currentThread, null))
                error("Rendering twice at the same time")
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        val events = events ?: return false
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
        val events = events ?: return false
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
                    handleActionButton(event, events, controller)
                    when (event.action) {
                        MotionEvent.ACTION_MOVE ->
                            handleMousePointer(
                                event, densityX, densityY, controller, events
                            )
                        MotionEvent.ACTION_SCROLL ->
                            handleMouseScroll(
                                event, controller, events
                            )
                    }
                }
            }
        }
        return true
    }

    override fun onCapturedPointerEvent(event: MotionEvent): Boolean {
        super.onCapturedPointerEvent(event)
        val events = events ?: return false
        val controller = devices[event.deviceId]
        val device = event.device ?: return false
        when (controller) {
            is AndroidControllerDesktop -> {
                if (device.isType(InputDevice.SOURCE_MOUSE_RELATIVE)) {
                    handleActionButton(event, events, controller)
                    when (event.action) {
                        MotionEvent.ACTION_MOVE ->
                            handleMousePointerRelative(
                                event, densityX, densityY, controller, events
                            )
                        MotionEvent.ACTION_SCROLL ->
                            handleMouseScroll(
                                event, controller, events
                            )
                    }
                }
            }
        }
        return true
    }

    private fun handleActionButton(
        event: MotionEvent,
        events: EventDispatcher,
        controller: AndroidControllerDesktop
    ) {
        if (android.os.Build.VERSION.SDK_INT >= 23) when (event.action) {
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
        } else when (event.action) {
            MotionEvent.ACTION_BUTTON_PRESS, MotionEvent.ACTION_BUTTON_RELEASE -> {
                controller.addButtonState(event.buttonState, events)
            }
        }
    }

    override fun onKeyDown(
        keyCode: Int,
        event: KeyEvent
    ): Boolean {
        super.onKeyDown(keyCode, event)
        val events = events ?: return false
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
        val events = events ?: return false
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

    fun resetGL() {
        reset.set(true)
    }

    fun isRenderCall() = Thread.currentThread() === renderThread

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
    controller.addDelta(event.x / densityX, event.y / densityY, events)
}

private fun handleMouseScroll(
    event: MotionEvent,
    controller: AndroidControllerDesktop,
    events: EventDispatcher
) {
    controller.addScroll(
        event.getAxisValue(MotionEvent.AXIS_HSCROLL).toDouble(),
        event.getAxisValue(MotionEvent.AXIS_VSCROLL).toDouble(),
        1, events
    )
}
