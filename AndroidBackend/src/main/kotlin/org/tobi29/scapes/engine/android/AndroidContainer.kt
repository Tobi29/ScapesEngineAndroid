package org.tobi29.scapes.engine.android

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import org.tobi29.scapes.engine.Container
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.android.openal.AndroidOpenAL
import org.tobi29.scapes.engine.android.opengles.GLAndroidGLES
import org.tobi29.scapes.engine.android.opengles.GOSAndroidGLES
import org.tobi29.scapes.engine.backends.openal.openal.OpenALSoundSystem
import org.tobi29.scapes.engine.graphics.Font
import org.tobi29.scapes.engine.gui.GuiController
import org.tobi29.scapes.engine.utils.AtomicReference
import org.tobi29.scapes.engine.utils.io.ByteBuffer
import org.tobi29.scapes.engine.utils.io.IOException
import org.tobi29.scapes.engine.utils.io.NativeByteBufferProvider
import org.tobi29.scapes.engine.utils.io.ReadSource
import org.tobi29.scapes.engine.utils.io.filesystem.FileCache
import org.tobi29.scapes.engine.utils.io.filesystem.FilePath
import org.tobi29.scapes.engine.utils.logging.KLogging
import java.io.File

class AndroidContainer(
        override val engine: ScapesEngine,
        context: Context,
        private val handler: Handler,
        private val typefaceCache: FilePath,
        private val stop: () -> Unit
) : Container {
    val view get() = attachedView.get()
    override val gos = GOSAndroidGLES(engine)
    override val sounds = OpenALSoundSystem(engine, AndroidOpenAL(context), 16,
            50.0)
    override val formFactor = Container.FormFactor.PHONE
    override val containerWidth get() = view?.containerWidth ?: 1
    override val containerHeight get() = view?.containerHeight ?: 1
    private val gl = GLAndroidGLES(gos)
    private val attachedView = AtomicReference<ScapesEngineView?>()
    private var renderThread = AtomicReference<Thread?>(null)

    fun render(delta: Double,
               view: ScapesEngineView,
               width: Int,
               height: Int) {
        if (view != attachedView.get()) {
            throw IllegalStateException("Different view attached")
        }
        val currentThread = Thread.currentThread()
        if (!renderThread.compareAndSet(null, currentThread)) {
            throw IllegalStateException("Rendering twice at the same time")
        }
        try {
            engine.graphics.render(gl, delta, width, height)
        } finally {
            if (!renderThread.compareAndSet(currentThread, null)) {
                throw IllegalStateException("Rendering twice at the same time")
            }
        }
    }

    fun attachView(view: ScapesEngineView) {
        if (!attachedView.compareAndSet(null, view)) {
            throw IllegalStateException("A view is already attached")
        }
        view.setRenderer(ScapesEngineRenderer(this))
    }

    fun detachView(view: ScapesEngineView) {
        if (!attachedView.compareAndSet(view, null)) {
            throw IllegalStateException("No or a different view is attached")
        }
        engine.graphics.reset()
    }

    override fun updateContainer() {
    }

    override fun update(delta: Double) {
        view?.let { view ->
            while (view.deviceEvents.isNotEmpty()) {
                engine.events.fire(view.deviceEvents.poll())
            }
        }
    }

    override fun loadFont(asset: ReadSource): Font? {
        try {
            val cache = typefaceCache
            var typeface: Typeface? = null
            while (typeface == null) {
                val file = FileCache.retrieve(cache,
                        FileCache.store(cache, asset))
                try {
                    if (file != null) {
                        typeface = Typeface.createFromFile(File(file.toUri()))
                    }
                } catch (e: RuntimeException) {
                    logger.warn(e) { "Failed to load typeface from cache" }
                }
            }
            return AndroidFont(typeface)
        } catch (e: IOException) {
            logger.error(e) { "Failed to load font" }
        }
        return null
    }

    override fun allocate(capacity: Int): ByteBuffer {
        return NativeByteBufferProvider.allocate(capacity)
    }

    override fun reallocate(buffer: ByteBuffer): ByteBuffer {
        return NativeByteBufferProvider.reallocate(buffer)
    }

    override fun run() {
        throw UnsupportedOperationException(
                "Android backend should be called from GLThread loop")
    }

    override fun clipboardCopy(value: String) {
    }

    override fun clipboardPaste(): String {
        return ""
    }

    override fun message(messageType: Container.MessageType,
                         title: String,
                         message: String) {
        val view = view ?: return
        handler.post {
            val context = view.context
            AlertDialog.Builder(context).setTitle(title).setMessage(
                    message).setPositiveButton(
                    "OK") { _, _ -> }.create().show()
        }
    }

    override fun dialog(title: String,
                        text: GuiController.TextFieldData,
                        multiline: Boolean) {
        val view = view ?: return
        handler.post {
            val context = view.context
            val editText = EditText(context)
            editText.setText(text.text.toString())
            val dialog = AlertDialog.Builder(context).setTitle(
                    title).setView(
                    editText).setPositiveButton("Done") { _, _ ->
                if (text.text.isNotEmpty()) {
                    text.text.clear()
                }
                text.text.append(editText.text)
                text.cursor = text.text.length
            }.create()
            dialog.show()
            editText.requestFocus()
            val imm = editText.context.getSystemService(
                    Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
