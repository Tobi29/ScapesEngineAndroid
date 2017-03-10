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

import android.content.ComponentName
import android.content.ServiceConnection
import android.opengl.GLSurfaceView
import android.os.IBinder
import org.tobi29.scapes.engine.utils.Sync
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

internal class ScapesEngineConnection(val activity: ScapesEngineActivity) : ServiceConnection {
    private val sync = Sync(60.0, 5000000000L, false, "Rendering")
    internal var view: ScapesEngineView? = null
        private set

    override fun onServiceConnected(name: ComponentName,
                                    service: IBinder) {
        val engine = (service as ScapesEngineService.ScapesBinder).get()
        engine.activity(activity)
        val view = ScapesEngineView(engine, activity).apply {
            setRenderer(object : GLSurfaceView.Renderer {
                override fun onSurfaceCreated(gl: GL10,
                                              config: EGLConfig) {
                    service.let { service ->
                        engine.engine?.graphics?.reset()
                        sync.init()
                    }
                }

                override fun onSurfaceChanged(gl: GL10,
                                              width: Int,
                                              height: Int) {
                    engine.setResolution(width, height)
                }

                override fun onDrawFrame(gl: GL10) {
                    engine.let { service ->
                        service.render(sync.delta())
                        sync.tick()
                    }
                }
            })
        }
        this.view = view
        activity.setContentView(view)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        activity.setContentView(null)
        view = null
    }
}