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

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import org.tobi29.scapes.engine.gui.GlyphRenderer
import kotlin.math.roundToInt

internal class AndroidGlyphRenderer(
    private val typeface: Typeface,
    size: Int
) : GlyphRenderer {
    private val size = size.toDouble()
    private val tiles: Int
    private val pageTiles: Int
    private val pageTileBits: Int
    private val pageTileMask: Int
    private val glyphSize: Int
    private val imageSize: Int
    private val renderX: Int
    private val renderY: Int
    private val tileSize: Double

    init {
        val tileBits = when {
            size < 16 -> 4
            size < 32 -> 3
            size < 64 -> 2
            size < 128 -> 1
            else -> 0
        }
        tiles = 1 shl tileBits
        pageTileBits = tileBits shl 1
        pageTileMask = (1 shl pageTileBits) - 1
        pageTiles = 1 shl pageTileBits
        tileSize = 1.0 / tiles
        glyphSize = size shl 1
        imageSize = glyphSize shl tileBits
        renderX = (size * 0.5).roundToInt()
        renderY = (size * 1.4).roundToInt()
    }

    override fun pageInfo(id: Int): GlyphRenderer.GlyphPage {
        val width = IntArray(pageTiles)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.typeface = typeface
        paint.textSize = size.toFloat()
        val offset = id shl pageTileBits
        val singleWidth = FloatArray(1)
        for (i in 0..(pageTiles - 1)) {
            val c = (i + offset).toChar()
            val str = String(charArrayOf(c))
            paint.getTextWidths(str, singleWidth)
            width[i] = (singleWidth[0]).roundToInt()
        }
        return GlyphRenderer.GlyphPage(width, imageSize, tiles, tileSize)
    }

    override suspend fun page(id: Int): ByteArray {
        val bitmap = Bitmap.createBitmap(
            imageSize, imageSize,
            Bitmap.Config.ARGB_8888
        )
        bitmap.density = 96
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = 0xFFFFFFFF.toInt()
        paint.typeface = typeface
        paint.textSize = size.toFloat()
        var i = 0
        val offset = id shl pageTileBits
        for (y in 0 until tiles) {
            val yy = y * glyphSize + renderY
            for (x in 0 until tiles) {
                val xx = x * glyphSize + renderX
                val c = (i + offset).toChar()
                val str = String(charArrayOf(c))
                canvas.drawText(str, xx.toFloat(), yy.toFloat(), paint)
                i++
            }
        }
        val pixels = IntArray(imageSize * imageSize)
        bitmap.getPixels(pixels, 0, imageSize, 0, 0, imageSize, imageSize)
        val buffer = ByteArray(imageSize * imageSize * 4)
        i = 0
        var j = 0
        for (y in 0 until imageSize) {
            for (x in 0 until imageSize) {
                repeat(3) { buffer[j++] = 0xFF.toByte() }
                buffer[j++] = (pixels[i++] ushr 24).toByte()
            }
        }
        return buffer
    }

    override fun pageID(character: Char) = character.toInt() shr pageTileBits

    override fun pageCode(character: Char) = character.toInt() and pageTileMask
}
