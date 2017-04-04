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
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import kotlin.system.exitProcess

abstract class ScapesEngineActivity : GLActivity(), Crashable {
    private val taskExecutor = TaskExecutor(this, "Activity")
    private val handler = Handler()
    private var fileConsumer: ((String, ReadableByteStream) -> Unit)? = null
    private var container: AndroidActivityContainer? = null

    abstract fun onCreateEngine(): Pair<(ScapesEngine) -> Game, MutableTagMap>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view?.setRenderer(object : ScapesEngineRenderer() {
            override val container get() = this@ScapesEngineActivity.container
        })
        val cache = path(cacheDir.toString()).resolve("AndroidTypeface")
        FileCache.check(cache)
        val (game, configMap) = onCreateEngine()
        val engine = ScapesEngine(game, { engine ->
            AndroidActivityContainer(engine, cache).also { container = it }
        }, taskExecutor, configMap)
        engine.start()
    }

    override fun onResume() {
        super.onResume()
        container?.engine?.start()
    }

    override fun onPause() {
        super.onPause()
        container?.engine?.halt()
    }

    override fun onBackPressed() {
        container?.engine?.guiStack?.fireAction(GuiAction.BACK)
    }

    override fun onDestroy() {
        super.onDestroy()
        container?.engine?.dispose()
        taskExecutor.shutdown()
        container = null
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

    override fun crash(e: Throwable): Nothing {
        try {
            logger.error { "Engine crashed: $e" }
            e.printStackTrace()
        } finally {
            exitProcess(1)
        }
    }

    inner class AndroidActivityContainer(engine: ScapesEngine,
                                         typefaceCache: FilePath) : AndroidContainer(
            engine, handler, typefaceCache) {
        override val view get() = this@ScapesEngineActivity.view

        override fun openFileDialog(type: FileType,
                                    title: String,
                                    multiple: Boolean,
                                    result: (String, ReadableByteStream) -> Unit) {
            handler.post {
                openFileDialog(type, multiple, result)
            }
        }

        override fun stop() {
            handler.post {
                finishAndRemoveTask()
            }
        }
    }

    companion object : KLogging()
}
