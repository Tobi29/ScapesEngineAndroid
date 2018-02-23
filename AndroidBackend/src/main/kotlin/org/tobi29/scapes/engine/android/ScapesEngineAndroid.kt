package org.tobi29.scapes.engine.android

import android.content.Context
import android.graphics.Typeface
import org.tobi29.io.*
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
    override fun allocateNative(size: Int): ByteViewE =
        ByteBufferNative(size).viewE

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
