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

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.provider.OpenableColumns
import mu.KLogging
import org.tobi29.scapes.engine.gui.GuiAction
import org.tobi29.scapes.engine.input.FileType
import org.tobi29.scapes.engine.utils.Sync
import org.tobi29.scapes.engine.utils.io.BufferedReadChannelStream
import org.tobi29.scapes.engine.utils.io.ReadableByteStream
import org.tobi29.scapes.engine.utils.math.round
import java.io.IOException
import java.nio.channels.Channels
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class ScapesEngineActivity : Activity() {
    private val sync = Sync(60.0, 5000000000L, false, "Rendering")
    private val handler = Handler()
    private var serviceIntent: Intent? = null
    private var fileConsumer: ((String, ReadableByteStream) -> Unit)? = null
    private var widthSize = 0
    private var heightSize = 0
    private var widthResolution = 0
    private var heightResolution = 0
    private var containerResized = false
    private val connection = ScapesEngineConnection()
    internal var view: ScapesEngineView? = null

    protected abstract fun service(): Class<out ScapesEngineService>

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ScapesEngineView(this).apply {
            setRenderer(object : GLSurfaceView.Renderer {
                override fun onSurfaceCreated(gl: GL10,
                                              config: EGLConfig) {
                    connection.engine?.engine?.graphics?.reset()
                    sync.init()
                }

                override fun onSurfaceChanged(gl: GL10,
                                              width: Int,
                                              height: Int) {
                    val density = density
                    widthSize = round(width / density)
                    heightSize = round(height / density)
                    widthResolution = width
                    heightResolution = height
                    containerResized = true
                }

                override fun onDrawFrame(gl: GL10) {
                    connection.engine?.run {
                        if (containerResized) {
                            setResolution(widthSize, heightSize,
                                    widthResolution, heightResolution)
                            containerResized = false
                        }
                        render(sync.delta(), this@apply)
                    }
                    sync.tick()
                }
            })
        }
        setContentView(view)
        serviceIntent = Intent(this, service())
        startService(serviceIntent)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }

    public override fun onResume() {
        super.onResume()
        view?.onResume()
        assert(serviceIntent != null)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }

    public override fun onPause() {
        super.onPause()
        view?.onPause()
        unbindService(connection)
    }

    override fun onBackPressed() {
        connection.engine?.engine?.guiStack?.fireAction(GuiAction.BACK)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceIntent = null
        view = null
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            10 -> if (resultCode == Activity.RESULT_OK && data != null) {
                fileConsumer?.let { consumer ->
                    val file = data.data
                    if (file == null) {
                        val clipData = data.clipData
                        if (clipData != null) {
                            val count = clipData.itemCount
                            for (i in 0..count - 1) {
                                acceptFile(consumer,
                                        clipData.getItemAt(i).uri)
                            }
                        }
                    } else {
                        acceptFile(consumer, file)
                    }
                    fileConsumer = null
                }
            }
        }
    }

    private fun acceptFile(consumer: (String, ReadableByteStream) -> Unit,
                           file: Uri) {
        try {
            contentResolver.openInputStream(file)?.use { stream ->
                contentResolver.query(file, null, null, null,
                        null)?.use { cursor ->
                    cursor.moveToFirst()
                    val name = cursor.getString(
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    consumer.invoke(name,
                            BufferedReadChannelStream(
                                    Channels.newChannel(stream)))
                }
            }
        } catch (e: IOException) {
            logger.error(e) { "Failed to apply picked file" }
        }

    }

    fun openFileDialog(type: FileType,
                       multiple: Boolean,
                       result: (String, ReadableByteStream) -> Unit) {
        handler.post {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple)
            intent.type = "*/*"
            if (type == FileType.IMAGE) {
                val types = arrayOf("image/png")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, types)
            } else if (type == FileType.MUSIC) {
                val types = arrayOf("audio/mpeg", "audio/x-wav",
                        "application/ogg")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, types)
            }
            fileConsumer = result
            startActivityForResult(intent, 10)
        }
    }

    private inner class ScapesEngineConnection : ServiceConnection {
        internal var engine: ScapesEngineService? = null

        override fun onServiceConnected(name: ComponentName,
                                        service: IBinder) {
            val engine = (service as ScapesEngineService.ScapesBinder).service
            if (engine == null) {
                finishAndRemoveTask()
            } else {
                engine.activity(this@ScapesEngineActivity)
                this.engine = engine
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            setContentView(null)
            engine = null
        }
    }

    companion object : KLogging()
}
