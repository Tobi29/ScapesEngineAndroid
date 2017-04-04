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

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import mu.KLogging
import org.tobi29.scapes.engine.Game
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.gui.GuiAction
import org.tobi29.scapes.engine.input.FileType
import org.tobi29.scapes.engine.utils.Crashable
import org.tobi29.scapes.engine.utils.io.ReadableByteStream
import org.tobi29.scapes.engine.utils.io.filesystem.FileCache
import org.tobi29.scapes.engine.utils.io.filesystem.FilePath
import org.tobi29.scapes.engine.utils.io.filesystem.path
import org.tobi29.scapes.engine.utils.tag.MutableTagMap
import org.tobi29.scapes.engine.utils.task.TaskExecutor
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.system.exitProcess

abstract class ScapesEngineService : Service(), Crashable {
    var container: AndroidContainer? = null
        private set
    private val taskExecutor = TaskExecutor(this, "Service")
    private val done = AtomicBoolean()
    private val handler = Handler()
    private var activity: ScapesEngineServiceActivity? = null

    fun activity(activity: ScapesEngineServiceActivity) {
        if (this.activity != null) {
            throw IllegalStateException(
                    "Trying to attach activity to already used service")
        }
        this.activity = activity
    }

    fun onBackPressed() {
        container?.engine?.guiStack?.fireAction(GuiAction.BACK)
    }

    abstract fun onCreateEngine(): Pair<(ScapesEngine) -> Game, MutableTagMap>

    override fun onCreate() {
        super.onCreate()
        val notification = Notification.Builder(this).build()
        startForeground(1, notification)
        val cache = path(cacheDir.toString()).resolve("AndroidTypeface")
        FileCache.check(cache)
        val (game, configMap) = onCreateEngine()
        val engine = ScapesEngine(game, { engine ->
            AndroidServiceContainer(engine, cache).also { container = it }
        }, taskExecutor, configMap)
        engine.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        done.set(true)
        activity?.finishAndRemoveTask()
        container?.engine?.dispose()
        taskExecutor.shutdown()
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

    override fun crash(e: Throwable): Nothing {
        try {
            logger.error { "Engine crashed: $e" }
            e.printStackTrace()
        } finally {
            exitProcess(1)
        }
    }

    inner class AndroidServiceContainer(engine: ScapesEngine,
                                        typefaceCache: FilePath) : AndroidContainer(
            engine, handler, typefaceCache) {
        override val view get() = activity?.view

        override fun openFileDialog(type: FileType,
                                    title: String,
                                    multiple: Boolean,
                                    result: (String, ReadableByteStream) -> Unit) {
            val activity = activity ?: return
            handler.post {
                activity.openFileDialog(type, multiple, result)
            }
        }

        override fun stop() {
            done.set(true)
            handler.post {
                activity?.finishAndRemoveTask()
                stopSelf()
            }
        }
    }

    inner class ScapesBinder : Binder() {
        val service = if (done.get()) null else this@ScapesEngineService
    }

    companion object : KLogging()
}
