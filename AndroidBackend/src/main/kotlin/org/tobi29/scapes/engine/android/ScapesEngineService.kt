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

import android.app.AlertDialog
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.opengl.GLSurfaceView
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import mu.KLogging
import org.tobi29.scapes.engine.Container
import org.tobi29.scapes.engine.Game
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.android.openal.AndroidOpenAL
import org.tobi29.scapes.engine.android.opengles.GLAndroidGLES
import org.tobi29.scapes.engine.android.opengles.GOSAndroidGLES
import org.tobi29.scapes.engine.backends.openal.openal.OpenALSoundSystem
import org.tobi29.scapes.engine.graphics.Font
import org.tobi29.scapes.engine.gui.GuiAction
import org.tobi29.scapes.engine.gui.GuiController
import org.tobi29.scapes.engine.input.ControllerDefault
import org.tobi29.scapes.engine.input.ControllerJoystick
import org.tobi29.scapes.engine.input.ControllerTouch
import org.tobi29.scapes.engine.input.FileType
import org.tobi29.scapes.engine.utils.EventDispatcher
import org.tobi29.scapes.engine.utils.io.ReadableByteStream
import org.tobi29.scapes.engine.utils.io.filesystem.FilePath
import org.tobi29.scapes.engine.utils.io.filesystem.path
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.atomic.AtomicBoolean

abstract class ScapesEngineService : Service() {
    private var container: AndroidContainer? = null
    private val done = AtomicBoolean()
    private val handler = Handler()
    private var activity: ScapesEngineActivity? = null
    private var lastView: WeakReference<GLSurfaceView>? = null

    fun activity(activity: ScapesEngineActivity) {
        if (this.activity != null) {
            throw IllegalStateException(
                    "Trying to attach activity to already used service")
        }
        this.activity = activity
    }

    fun resetGraphics() {
        container?.engine?.graphics?.reset()
    }

    fun render(delta: Double,
               view: GLSurfaceView,
               width: Int,
               height: Int) {
        container?.render(delta, view, width, height)
    }

    fun onBackPressed() {
        container?.engine?.guiStack?.fireAction(GuiAction.BACK)
    }

    abstract fun onCreateEngine(home: FilePath): (ScapesEngine) -> Game

    override fun onCreate() {
        super.onCreate()
        val notification = Notification.Builder(this).build()
        startForeground(1, notification)
        val home = path(filesDir.toString())
        val cache = path(cacheDir.toString())
        val game = onCreateEngine(home)
        val engine = ScapesEngine(game, { p1 ->
            AndroidContainer(p1).also { container = it }
        }, home, cache, true)
        engine.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        done.set(true)
        activity?.finishAndRemoveTask()
        container?.engine?.dispose()
        activity = null
        container = null
        logger.info { "Service destroyed" }
    }

    override fun onBind(intent: Intent): IBinder? {
        return ScapesBinder()
    }

    override fun onUnbind(intent: Intent): Boolean {
        activity = null
        return true
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    inner class AndroidContainer(
            internal val engine: ScapesEngine
    ) : Container, ControllerTouch {
        override val gos = GOSAndroidGLES(engine)
        override val sounds = OpenALSoundSystem(engine, AndroidOpenAL(), 16,
                50.0)
        override val events = EventDispatcher()
        override val formFactor = Container.FormFactor.PHONE
        override val containerWidth get() = activity?.view?.containerWidth ?: 1
        override val containerHeight get() = activity?.view?.containerHeight ?: 1
        private val gl = GLAndroidGLES(gos)

        fun render(delta: Double,
                   view: GLSurfaceView,
                   width: Int,
                   height: Int) {
            if (view != lastView?.get()) {
                lastView = WeakReference(view)
                engine.graphics.reset()
            }
            engine.graphics.render(gl, delta, width, height)
        }

        override fun updateContainer() {
        }

        override fun update(delta: Double) {
            poll()
        }

        override fun controller(): ControllerDefault? {
            return null
        }

        override fun joysticks(): Collection<ControllerJoystick> {
            return emptyList()
        }

        override fun joysticksChanged(): Boolean {
            return false
        }

        override fun touch(): ControllerTouch? {
            return this
        }

        override fun loadFont(asset: String): Font? {
            try {
                var font = engine.files[asset + ".otf"].get()
                if (!font.exists()) {
                    font = engine.files[asset + ".ttf"].get()
                }
                var typeface: Typeface? = null
                while (typeface == null) {
                    val file = engine.fileCache.retrieve(
                            engine.fileCache.store(font, "AndroidTypeface"))
                    try {
                        if (file != null) {
                            typeface = Typeface.createFromFile(
                                    File(file.toUri()))
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
            return ByteBuffer.allocateDirect(capacity).order(
                    ByteOrder.nativeOrder())
        }

        override fun run() {
            throw UnsupportedOperationException(
                    "Android backend should be called from GLThread loop")
        }

        override fun stop() {
            done.set(true)
            handler.post {
                activity?.finishAndRemoveTask()
                stopSelf()
            }
        }

        override fun clipboardCopy(value: String) {
        }

        override fun clipboardPaste(): String {
            return ""
        }

        override fun openFileDialog(type: FileType,
                                    title: String,
                                    multiple: Boolean,
                                    result: Function2<String, ReadableByteStream, Unit>) {
            activity?.openFileDialog(type, multiple, result)
        }

        override fun saveFileDialog(extensions: Array<Pair<String, String>>,
                                    title: String): FilePath? {
            return null
        }

        override fun message(messageType: Container.MessageType,
                             title: String,
                             message: String) {
            handler.post {
                val activity = this@ScapesEngineService.activity ?: return@post
                val context = activity.view?.context
                AlertDialog.Builder(context).setTitle(title).setMessage(
                        message).setPositiveButton(
                        "OK") { _, _ -> }.create().show()
            }
        }

        override fun dialog(title: String,
                            text: GuiController.TextFieldData,
                            multiline: Boolean) {
            handler.post {
                val activity = this@ScapesEngineService.activity ?: return@post
                val context = activity.view?.context
                val editText = EditText(context)
                editText.setText(text.text.toString())
                val dialog = AlertDialog.Builder(context).setTitle(
                        title).setView(
                        editText).setPositiveButton("Done") { _, _ ->
                    if (text.text.isNotEmpty()) {
                        text.text.delete(0, Int.MAX_VALUE)
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

        override fun openFile(path: FilePath) {
        }

        override fun fingers(): Sequence<ControllerTouch.Tracker> {
            val view = activity?.view ?: return emptySequence()
            return view.fingers.values.asSequence()
        }

        override val isActive: Boolean
            get() = activity != null

        override fun poll() {
        }
    }

    inner class ScapesBinder : Binder() {
        val service = if (done.get()) null else this@ScapesEngineService
    }

    companion object : KLogging()
}
