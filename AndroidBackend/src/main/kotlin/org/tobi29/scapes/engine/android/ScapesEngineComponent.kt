/*
 * Copyright 2012-2018 Tobi29
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

import android.content.Context
import android.os.Handler
import kotlinx.coroutines.experimental.runBlocking
import org.tobi29.coroutines.defaultBackgroundExecutor
import org.tobi29.io.filesystem.FileCache
import org.tobi29.io.filesystem.path
import org.tobi29.io.tag.MutableTagMap
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.gui.GuiAction
import org.tobi29.scapes.engine.gui.GuiStyle

class ScapesEngineComponent(
    context: Context,
    private val handler: Handler,
    onCreateEngine: () -> Pair<(ScapesEngine) -> GuiStyle, MutableTagMap>,
    onInitEngine: (ScapesEngine) -> Unit,
    onStop: () -> Unit
) {
    private var container: Pair<AndroidContainer, ScapesEngine>? = null
    private var _view: ScapesEngineView? = null
    val view: ScapesEngineView get() = _view ?: error("Component destroyed")

    init {
        val cache = path(context.cacheDir.toString()).resolve("AndroidTypeface")
        FileCache.check(cache)
        val (defaultGuiStyle, configMap) = onCreateEngine()
        val container = AndroidContainer(ScapesEngineAndroid(context, cache),
            { handler.post { onStop() } })
        val engine = ScapesEngine(
            container, defaultGuiStyle,
            defaultBackgroundExecutor, configMap
        )
        this.container = container to engine
        onInitEngine(engine)
        _view = ScapesEngineView(context, engine).also {
            container.attachView(it)
        }
        engine.start()
    }

    fun onPause() {
        view.onPause()
        runBlocking { container?.second?.halt() }
    }

    fun onResume() {
        container?.second?.start()
        view.onResume()
    }

    fun onBackPressed() {
        container?.second?.guiStack?.fireAction(GuiAction.BACK)
    }

    fun onDestroy() {
        container?.let { (container, _) ->
            container.detachView(view)
        }
        view.dispose()
        _view = null
        container?.let {
            runBlocking { it.second.dispose() }
            container = null
        }
    }
}
