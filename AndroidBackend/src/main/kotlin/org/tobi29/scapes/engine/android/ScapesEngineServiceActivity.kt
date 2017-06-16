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
import org.tobi29.scapes.engine.utils.logging.KLogging

abstract class ScapesEngineServiceActivity : Activity() {
    private var serviceIntent: Intent? = null
    private val connection = ScapesEngineConnection()
    private var view: ScapesEngineView? = null

    protected abstract fun service(): Class<out ScapesEngineService>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceIntent = Intent(this, service())
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onBackPressed() {
        println("OK")
        connection.engine?.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        view?.onPause()
    }

    override fun onResume() {
        super.onResume()
        view?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        disposeView()
        serviceIntent = null
    }

    private fun initView(container: AndroidContainer): ScapesEngineView {
        disposeView()
        return ScapesEngineView(this@ScapesEngineServiceActivity).also {
            container.attachView(it)
            setContentView(it)
            view = it
        }
    }

    private fun disposeView() {
        view?.let {
            connection.engine?.container?.detachView(it)
            it.dispose()
            view = null
        }
    }

    private inner class ScapesEngineConnection : ServiceConnection {
        internal var engine: ScapesEngineService? = null

        override fun onServiceConnected(name: ComponentName,
                                        service: IBinder) {
            disposeView()
            val engine = (service as ScapesEngineService.ScapesBinder).service
            val container = engine?.container
            if (container == null) {
                finishAndRemoveTask()
            } else {
                initView(container)
                engine.activity(this@ScapesEngineServiceActivity)
                this.engine = engine
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            disposeView()
            engine = null
        }
    }

    companion object : KLogging()
}
