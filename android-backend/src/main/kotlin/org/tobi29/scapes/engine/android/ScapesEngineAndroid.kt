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
import android.graphics.Typeface
import org.tobi29.io.IOException
import org.tobi29.io.ReadSource
import org.tobi29.io.filesystem.FileCache
import org.tobi29.io.filesystem.FilePath
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.ScapesEngineBackend
import org.tobi29.scapes.engine.android.openal.AndroidOpenAL
import org.tobi29.scapes.engine.backends.openal.openal.OpenALSoundSystem
import org.tobi29.scapes.engine.graphics.Font
import org.tobi29.scapes.engine.sound.SoundSystem

class ScapesEngineAndroid(
    private val context: Context,
    private val typefaceCache: FilePath
) : ScapesEngineBackend {
    override suspend fun loadFont(asset: ReadSource): Font {
        val cache = typefaceCache
        var typeface: Typeface? = null
        while (typeface == null) {
            val file = asset.readAsync {
                FileCache.retrieve(cache, FileCache.store(cache, it))
            }
            try {
                if (file != null) {
                    typeface = Typeface.createFromFile(file.toFile())
                }
            } catch (e: RuntimeException) {
                throw IOException(e)
            }
        }
        return AndroidFont(typeface)
    }

    override fun createSoundSystem(engine: ScapesEngine): SoundSystem =
        OpenALSoundSystem(engine, AndroidOpenAL(context), 16, 50.0)
}
