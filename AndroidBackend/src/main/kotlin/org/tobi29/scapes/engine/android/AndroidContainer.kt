package org.tobi29.scapes.engine.android

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import org.tobi29.io.ByteBufferNative
import org.tobi29.io.ByteViewE
import org.tobi29.io.viewE
import org.tobi29.logging.KLogging
import org.tobi29.scapes.engine.Container
import org.tobi29.scapes.engine.ScapesEngineBackend
import org.tobi29.scapes.engine.backends.opengles.GLESHandle
import org.tobi29.scapes.engine.backends.opengles.GLESImpl
import org.tobi29.scapes.engine.gui.GuiController
import org.tobi29.stdex.atomic.AtomicBoolean
import org.tobi29.stdex.atomic.AtomicReference
import org.tobi29.utils.sleep

class AndroidContainer(
    backend: ScapesEngineBackend,
    context: Context,
    private val handler: Handler,
    private val stop: () -> Unit
) : Container, ScapesEngineBackend by backend {
    val view get() = attachedView.get()
    private val glh = GLESHandle(this)
    override val gos get() = glh
    override val formFactor = Container.FormFactor.PHONE
    override val containerWidth get() = view?.containerWidth ?: 1
    override val containerHeight get() = view?.containerHeight ?: 1
    private val gl = GLESImpl(gos)
    private val attachedView = AtomicReference<ScapesEngineView?>()
    private var renderThread = AtomicReference<Thread?>(null)
    private val reset = AtomicBoolean(false)

    fun render(
        delta: Double,
        view: ScapesEngineView,
        width: Int,
        height: Int
    ) {
        val currentView = attachedView.get()
                ?: throw IllegalStateException("No view attached")
        if (view != currentView) {
            throw IllegalStateException("Different view attached")
        }
        val currentThread = Thread.currentThread()
        if (!renderThread.compareAndSet(null, currentThread)) {
            throw IllegalStateException("Rendering twice at the same time")
        }
        if (reset.getAndSet(false)) view.engine.graphics.reset()
        try {
            while (!view.engine.graphics.render(
                    gl, delta, width, height,
                    containerWidth, containerHeight
                )) {
                sleep(1L)
            }
        } finally {
            if (!renderThread.compareAndSet(currentThread, null)) {
                throw IllegalStateException("Rendering twice at the same time")
            }
        }
    }

    fun attachView(view: ScapesEngineView) {
        if (!attachedView.compareAndSet(null, view))
            throw IllegalStateException("A view is already attached")
        view.setRenderer(ScapesEngineRenderer(this))
    }

    fun detachView(view: ScapesEngineView) {
        if (attachedView.getAndSet(null) != view)
            throw IllegalStateException("No or a different view is attached")
        view.engine.graphics.reset()
    }

    fun resetGL() {
        reset.set(true)
    }

    override fun updateContainer() {}

    override fun update(delta: Double) {}

    override fun allocateNative(size: Int): ByteViewE =
        ByteBufferNative(size).viewE

    override fun clipboardCopy(value: String) {
    }

    override fun clipboardPaste(): String {
        return ""
    }

    override fun message(
        messageType: Container.MessageType,
        title: String,
        message: String
    ) {
        val view = view ?: return
        handler.post {
            val context = view.context
            AlertDialog.Builder(context).setTitle(title).setMessage(
                message
            ).setPositiveButton(
                "OK"
            ) { _, _ -> }.create().show()
        }
    }

    override fun dialog(
        title: String,
        text: GuiController.TextFieldData,
        multiline: Boolean
    ) {
        val view = view ?: return
        handler.post {
            val context = view.context
            val editText = EditText(context)
            editText.setText(text.text.toString())
            val dialog = AlertDialog.Builder(context).setTitle(
                title
            ).setView(
                editText
            ).setPositiveButton("Done") { _, _ ->
                if (text.text.isNotEmpty()) {
                    text.text.clear()
                }
                text.text.append(editText.text)
                text.cursor = text.text.length
                text.dirty.set(true)
            }.create()
            dialog.show()
            editText.requestFocus()
            val imm = editText.context.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
        }
    }

    override fun isRenderCall() = Thread.currentThread() === renderThread

    override fun stop() = stop.invoke()

    companion object : KLogging() {
        init {
            AndroidKeyMap.touch()
        }
    }
}
