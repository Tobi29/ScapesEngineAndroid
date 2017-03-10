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
import org.tobi29.scapes.engine.android.opengles.GLAndroidGL
import org.tobi29.scapes.engine.backends.openal.openal.OpenALSoundSystem
import org.tobi29.scapes.engine.graphics.Font
import org.tobi29.scapes.engine.graphics.GL
import org.tobi29.scapes.engine.gui.GuiController
import org.tobi29.scapes.engine.input.ControllerDefault
import org.tobi29.scapes.engine.input.ControllerJoystick
import org.tobi29.scapes.engine.input.ControllerTouch
import org.tobi29.scapes.engine.input.FileType
import org.tobi29.scapes.engine.sound.SoundSystem
import org.tobi29.scapes.engine.utils.EventDispatcher
import org.tobi29.scapes.engine.utils.io.ReadableByteStream
import org.tobi29.scapes.engine.utils.io.filesystem.FilePath
import org.tobi29.scapes.engine.utils.io.filesystem.path
import org.tobi29.scapes.engine.utils.math.round
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

abstract class ScapesEngineService : Service(), Container, ControllerTouch {
    override val events = EventDispatcher()
    internal var engine: ScapesEngine? = null
        private set
    private val handler = Handler()
    private var gl: GL? = null
    private var sound: SoundSystem? = null
    private var activity: ScapesEngineActivity? = null
    private var width = 0
    private var height = 0
    private var widthResolution = 0
    private var heightResolution = 0
    private var containerResized = false
    internal val density: Double
        get() {
            return 3.0
        }

    fun activity(activity: ScapesEngineActivity) {
        if (this.activity != null) {
            throw IllegalStateException(
                    "Trying to attach activity to already used service")
        }
        this.activity = activity
    }

    fun setResolution(width: Int,
                      height: Int) {
        val density = density
        this.width = round(width / density)
        this.height = round(height / density)
        this.widthResolution = width
        this.heightResolution = height
        containerResized = true
    }

    fun render(delta: Double) {
        engine?.let {
            it.graphics.render(delta)
            containerResized = false
        }
    }

    abstract fun onCreateEngine(home: FilePath): (ScapesEngine) -> Game

    override fun onCreate() {
        super.onCreate()
        val notification = Notification.Builder(this).build()
        startForeground(1, notification)
        val home = path(filesDir.toString())
        val cache = path(cacheDir.toString())
        val game = onCreateEngine(home)
        ScapesEngine(game, { p1 ->
            engine = p1
            gl = GLAndroidGL(p1, this@ScapesEngineService)
            sound = OpenALSoundSystem(p1, AndroidOpenAL(), 16, 50.0)
            this@ScapesEngineService
        }, home, cache, true).start()
    }

    override fun onStartCommand(intent: Intent,
                                flags: Int,
                                startId: Int): Int {
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.finishAndRemoveTask()
        engine?.dispose()
        activity = null
        gl = null
        engine = null
        sound = null
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

    override fun formFactor(): Container.FormFactor {
        return Container.FormFactor.PHONE
    }

    override fun containerWidth(): Int {
        return width
    }

    override fun containerHeight(): Int {
        return height
    }

    override fun contentWidth(): Int {
        return widthResolution
    }

    override fun contentHeight(): Int {
        return heightResolution
    }

    override fun contentResized(): Boolean {
        return containerResized
    }

    override fun updateContainer() {
    }

    override fun update(delta: Double) {
        poll()
    }

    override fun gl(): GL {
        return gl ?: throw IllegalStateException("Service disposed")
    }

    override fun sound(): SoundSystem {
        return sound ?: throw IllegalStateException("Service disposed")
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
        val engine = engine ?: throw IllegalStateException("Service disposed")
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
        return ByteBuffer.allocateDirect(capacity).order(
                ByteOrder.nativeOrder())
    }

    override fun run() {
        throw UnsupportedOperationException(
                "Android backend should be called from GLThread loop")
    }

    override fun stop() {
        logger.info { "Stopping app..." }
        handler.post { stopSelf() }
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
    }

    override fun dialog(title: String,
                        text: GuiController.TextFieldData,
                        multiline: Boolean) {
        val activity = this.activity ?: return
        val context = activity.connection.view?.context
        handler.post {
            val editText = EditText(context)
            editText.setText(text.text.toString())
            val dialog = AlertDialog.Builder(context).setView(
                    editText).setPositiveButton("Done"
            ) { dialog, which ->
                if (text.text.isNotEmpty()) {
                    text.text.delete(0,
                            Int.MAX_VALUE)
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
        val view = activity?.connection?.view ?: return emptySequence()
        return view.fingers.values.asSequence()
    }

    override val isActive: Boolean
        get() = activity != null

    override fun poll() {
    }

    inner class ScapesBinder : Binder() {
        fun get(): ScapesEngineService {
            return this@ScapesEngineService
        }
    }

    companion object : KLogging()
}
