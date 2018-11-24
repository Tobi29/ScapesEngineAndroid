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

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.tobi29.io.filesystem.FileCache
import org.tobi29.io.tag.MutableTagMap
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.gui.GuiAction
import org.tobi29.scapes.engine.gui.GuiStyle

abstract class ScapesEngineFragment : Fragment() {
    private lateinit var engine: ScapesEngine
    private lateinit var container: AndroidContainer
    private var view: ScapesEngineView? = null
    private val handler = Handler()

    abstract fun onCreateEngine(): Pair<(ScapesEngine) -> GuiStyle, MutableTagMap>

    abstract fun onInitEngine(engine: ScapesEngine)

    open fun onStopEngine() {
        (_context as? Activity)?.run {
            if (android.os.Build.VERSION.SDK_INT >= 21) finishAndRemoveTask()
            else finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = (
                _context ?: error("Not attach during onCreate")
                ).applicationContext
        val (engine, container) = ScapesEngine(
            context, handler, ::onCreateEngine, ::onInitEngine, ::onStopEngine
        )
        this.engine = engine
        this.container = container
        engine.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = (
                _context ?: error("Not attach during onCreateView")
                ).applicationContext
        view?.let { this.container.detach(engine, it) }
        val view = ScapesEngineView(context)
        this.container.attach(engine, view)
        this.view = view
        return view
    }

    override fun onDetach() {
        super.onDetach()
        view?.let {
            container.detach(engine, it)
            view = null
        }
    }

    override fun onPause() {
        super.onPause()
        view?.onPause()
        runBlocking { engine.halt() }
    }

    override fun onResume() {
        super.onResume()
        engine.start()
        view?.onResume()
    }

    fun onBackPressed() {
        engine.guiStack.fireAction(GuiAction.BACK)
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.let {
            container.detach(engine, it)
            view = null
        }
        runBlocking { engine.dispose() }
    }
}

inline fun ScapesEngine(
    context: Context,
    handler: Handler,
    onCreateEngine: () -> Pair<(ScapesEngine) -> GuiStyle, MutableTagMap>,
    onInitEngine: (ScapesEngine) -> Unit,
    crossinline onStop: () -> Unit
): Pair<ScapesEngine, AndroidContainer> {
    val cache = context.cachePath.resolve("AndroidTypeface")
    FileCache.check(cache)
    val (defaultGuiStyle, configMap) = onCreateEngine()
    val container = AndroidContainer(ScapesEngineAndroid(context, cache),
        { handler.post { onStop() } })
    val engine = ScapesEngine(
        container, defaultGuiStyle,
        Dispatchers.Default, configMap
    )
    onInitEngine(engine)
    return engine to container
}
