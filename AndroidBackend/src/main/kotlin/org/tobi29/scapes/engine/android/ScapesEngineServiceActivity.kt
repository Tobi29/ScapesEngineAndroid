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
import android.os.Bundle
import android.os.IBinder
import org.tobi29.scapes.engine.input.FileType
import org.tobi29.scapes.engine.utils.io.ReadableByteStream
import org.tobi29.scapes.engine.utils.logging.KLogging

abstract class ScapesEngineServiceActivity : GLActivity() {
    private var serviceIntent: Intent? = null
    private var fileConsumer: ((String, ReadableByteStream) -> Unit)? = null
    private val connection = ScapesEngineConnection()

    protected abstract fun service(): Class<out ScapesEngineService>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view?.setRenderer(object : ScapesEngineRenderer() {
            override val container get() = connection.engine?.container
        })
        serviceIntent = Intent(this, service())
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onBackPressed() {
        connection.engine?.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        serviceIntent = null
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            10 -> if (resultCode == Activity.RESULT_OK && data != null) {
                fileConsumer?.let { consumer ->
                    acceptFile(contentResolver, consumer, data)
                    fileConsumer = null
                }
            }
        }
    }

    fun openFileDialog(type: FileType,
                       multiple: Boolean,
                       result: (String, ReadableByteStream) -> Unit) {
        fileConsumer = result
        startActivityForResult(openFileIntent(type, multiple), 10)
    }

    private inner class ScapesEngineConnection : ServiceConnection {
        internal var engine: ScapesEngineService? = null

        override fun onServiceConnected(name: ComponentName,
                                        service: IBinder) {
            val engine = (service as ScapesEngineService.ScapesBinder).service
            if (engine == null) {
                finishAndRemoveTask()
            } else {
                engine.activity(this@ScapesEngineServiceActivity)
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
