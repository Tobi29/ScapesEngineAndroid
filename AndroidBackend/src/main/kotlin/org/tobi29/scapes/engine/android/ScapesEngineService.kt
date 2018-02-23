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

import android.annotation.TargetApi
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import kotlinx.coroutines.experimental.runBlocking
import org.tobi29.coroutines.defaultBackgroundExecutor
import org.tobi29.io.filesystem.FileCache
import org.tobi29.io.filesystem.path
import org.tobi29.io.tag.MutableTagMap
import org.tobi29.logging.KLogging
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.gui.GuiAction
import org.tobi29.scapes.engine.gui.GuiStyle
import java.util.concurrent.atomic.AtomicBoolean

abstract class ScapesEngineService : Service() {
    var container: Pair<AndroidContainer, ScapesEngine>? = null
        private set
    private val done = AtomicBoolean()
    val handler = Handler()
    var activity: ScapesEngineServiceActivity? = null
    protected open val serviceNotificationChannel: String
        @TargetApi(26)
        get() = notificationChannelService

    fun activity(activity: ScapesEngineServiceActivity) {
        if (this.activity != null) {
            throw IllegalStateException(
                "Trying to attach activity to already used service"
            )
        }
        this.activity = activity
    }

    fun onBackPressed() {
        container?.second?.guiStack?.fireAction(GuiAction.BACK)
    }

    abstract fun onCreateEngine(): Pair<(ScapesEngine) -> GuiStyle, MutableTagMap>

    abstract fun onInitEngine(engine: ScapesEngine)

    override fun onCreate() {
        super.onCreate()
        val notification =
            if (Build.VERSION.SDK_INT >= 26) {
                Notification.Builder(this, serviceNotificationChannel)
            } else {
                @Suppress("DEPRECATION")
                Notification.Builder(this)
            }.build()
        startForeground(1, notification)
        val cache = path(cacheDir.toString()).resolve("AndroidTypeface")
        FileCache.check(cache)
        val (defaultGuiStyle, configMap) = onCreateEngine()
        val container = AndroidContainer(ScapesEngineAndroid(this, cache), this,
            handler, {
                done.set(true)
                handler.post {
                    activity?.finishAndRemoveTask()
                    stopSelf()
                }
            })
        val engine = ScapesEngine(
            container, defaultGuiStyle,
            defaultBackgroundExecutor, configMap
        )
        this.container = container to engine
        onInitEngine(engine)
        engine.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        done.set(true)
        activity?.let {
            it.finishAndRemoveTask()
            activity = null
        }
        container?.let { (_, engine) ->
            runBlocking { engine.dispose() }
            container = null
        }
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

    inner class ScapesBinder : Binder() {
        val service = if (done.get()) null else this@ScapesEngineService
    }

    companion object : KLogging()
}
