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
import android.os.Bundle
import android.os.Handler
import org.tobi29.scapes.engine.Game
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.gui.GuiAction
import org.tobi29.scapes.engine.utils.Crashable
import org.tobi29.scapes.engine.utils.io.filesystem.FileCache
import org.tobi29.scapes.engine.utils.io.filesystem.path
import org.tobi29.scapes.engine.utils.logging.KLogging
import org.tobi29.scapes.engine.utils.tag.MutableTagMap
import org.tobi29.scapes.engine.utils.task.TaskExecutor
import kotlin.system.exitProcess

abstract class ScapesEngineActivity : Activity(), Crashable {
    private val taskExecutor = TaskExecutor(this, "Activity")
    private var container: AndroidContainer? = null
    private var view: ScapesEngineView? = null
    val handler = Handler()

    abstract fun onCreateEngine(): Pair<(ScapesEngine) -> Game, MutableTagMap>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cache = path(cacheDir.toString()).resolve("AndroidTypeface")
        FileCache.check(cache)
        val (game, configMap) = onCreateEngine()
        val engine = ScapesEngine(game, { engine ->
            AndroidContainer(engine, handler, cache, {
                handler.post {
                    finishAndRemoveTask()
                }
            }).also { container = it }
        }, taskExecutor, configMap)
        val container = this.container
                ?: throw IllegalStateException("No container got created")
        view = ScapesEngineView(this).also {
            container.attachView(it)
            setContentView(it)
        }
        engine.start()
    }

    override fun onPause() {
        super.onPause()
        view?.onPause()
        container?.engine?.halt()
    }

    override fun onResume() {
        super.onResume()
        container?.engine?.start()
        view?.onResume()
    }

    override fun onBackPressed() {
        container?.engine?.guiStack?.fireAction(GuiAction.BACK)
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.let { container?.detachView(it) }
        container?.engine?.dispose()
        taskExecutor.shutdown()
        container = null
        view = null
    }

    override fun crash(e: Throwable): Nothing {
        try {
            logger.error { "Engine crashed: $e" }
            e.printStackTrace()
        } finally {
            exitProcess(1)
        }
    }

    companion object : KLogging()
}
