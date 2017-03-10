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
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.OpenableColumns
import mu.KLogging
import org.tobi29.scapes.engine.gui.GuiAction
import org.tobi29.scapes.engine.input.FileType
import org.tobi29.scapes.engine.utils.io.BufferedReadChannelStream
import org.tobi29.scapes.engine.utils.io.ReadableByteStream
import java.io.IOException
import java.nio.channels.Channels

abstract class ScapesEngineActivity : Activity() {
    private val handler = Handler()
    private var serviceIntent: Intent? = null
    private var fileConsumer: Function2<String, ReadableByteStream, Unit>? = null
    internal val connection = ScapesEngineConnection(this)

    protected abstract fun service(): Class<out ScapesEngineService>

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceIntent = Intent(this, service())
        startService(serviceIntent)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }

    public override fun onResume() {
        super.onResume()
        connection.view?.onResume()
        assert(serviceIntent != null)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }

    public override fun onPause() {
        super.onPause()
        connection.view?.onPause()
        unbindService(connection)
    }

    override fun onBackPressed() {
        connection.view?.service?.engine?.guiStack?.fireAction(GuiAction.BACK)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceIntent = null
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

    companion object : KLogging()
}
