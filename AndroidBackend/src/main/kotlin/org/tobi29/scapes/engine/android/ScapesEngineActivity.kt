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
import kotlinx.coroutines.experimental.runBlocking
import org.tobi29.coroutines.defaultBackgroundExecutor
import org.tobi29.io.filesystem.FileCache
import org.tobi29.io.filesystem.path
import org.tobi29.io.tag.MutableTagMap
import org.tobi29.logging.KLogging
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.gui.GuiAction
import org.tobi29.scapes.engine.gui.GuiStyle

abstract class ScapesEngineActivity : Activity() {
    private var container: Pair<AndroidContainer, ScapesEngine>? = null
    private var view: ScapesEngineView? = null
    val handler = Handler()

    abstract fun onCreateEngine(): Pair<(ScapesEngine) -> GuiStyle, MutableTagMap>

    abstract fun onInitEngine(engine: ScapesEngine)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cache = path(cacheDir.toString()).resolve("AndroidTypeface")
        FileCache.check(cache)
        val (defaultGuiStyle, configMap) = onCreateEngine()
        val container = AndroidContainer(ScapesEngineAndroid(this, cache), this,
                handler, {
            handler.post {
                finishAndRemoveTask()
            }
        })
        val engine = ScapesEngine(container, defaultGuiStyle,
                defaultBackgroundExecutor, configMap)
        this.container = container to engine
        onInitEngine(engine)
        view = ScapesEngineView(this, engine).also {
            container.attachView(it)
            setContentView(it)
        }
        engine.start()
    }

    override fun onPause() {
        super.onPause()
        view?.onPause()
        runBlocking { container?.second?.halt() }
    }

    override fun onResume() {
        super.onResume()
        container?.second?.start()
        view?.onResume()
    }

    override fun onBackPressed() {
        container?.second?.guiStack?.fireAction(GuiAction.BACK)
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.let {
            container?.let { (container, _) ->
                container.detachView(it)
            }
            it.dispose()
            view = null
        }
        container?.let {
            runBlocking { it.second.dispose() }
            container = null
        }
    }

    companion object : KLogging()
}
