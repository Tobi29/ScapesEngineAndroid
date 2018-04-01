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
import android.os.Bundle
import android.os.Handler
import org.tobi29.io.tag.MutableTagMap
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.gui.GuiStyle

abstract class ScapesEngineActivity : Activity() {
    private var component: ScapesEngineComponent? = null
    val handler = Handler()

    abstract fun onCreateEngine(): Pair<(ScapesEngine) -> GuiStyle, MutableTagMap>

    abstract fun onInitEngine(engine: ScapesEngine)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = ScapesEngineComponent(
            this, handler,
            onCreateEngine = {
                onCreateEngine()
            },
            onInitEngine = { engine ->
                onInitEngine(engine)
            },
            onStop = {
                finishAndRemoveTask()
            }
        ).also { component ->
            setContentView(component.view)
        }
    }

    override fun onPause() {
        super.onPause()
        component?.onPause()
    }

    override fun onResume() {
        super.onResume()
        component?.onResume()
    }

    override fun onBackPressed() {
        component?.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        component?.let {
            it.onDestroy()
            component = null
        }
    }
}
