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

package org.tobi29.scapes.engine.android.opengles

import android.opengl.GLES20
import android.opengl.GLES30
import org.tobi29.scapes.engine.graphics.FramebufferStatus
import org.tobi29.scapes.engine.graphics.RenderType
import org.tobi29.scapes.engine.utils.ThreadLocal
import org.tobi29.scapes.engine.utils.logging.KLogging
import org.tobi29.scapes.engine.utils.shader.CompiledShader
import org.tobi29.scapes.engine.utils.shader.Expression
import org.tobi29.scapes.engine.utils.shader.ShaderException
import org.tobi29.scapes.engine.utils.shader.Uniform
import org.tobi29.scapes.engine.utils.shader.backend.glsl.GLSLGenerator
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

internal object GLUtils : KLogging() {
    private val SHADER_GENERATOR = ThreadLocal {
        GLSLGenerator(GLSLGenerator.Version.GLES_300)
    }
    private val attachmentBuffer = ByteBuffer.allocateDirect(16 shl 2).order(
            ByteOrder.nativeOrder()).asIntBuffer().apply {
        for (i in 0..15) {
            put(GLES20.GL_COLOR_ATTACHMENT0 + i)
        }
        rewind()
    }

    fun renderType(renderType: RenderType): Int {
        when (renderType) {
            RenderType.TRIANGLES -> return GLES20.GL_TRIANGLES
            RenderType.LINES -> return GLES20.GL_LINES
            else -> throw IllegalArgumentException(
                    "Unknown render type: " + renderType)
        }
    }

    fun status(): FramebufferStatus {
        val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        when (status) {
            GLES20.GL_FRAMEBUFFER_COMPLETE -> return FramebufferStatus.COMPLETE
            GLES20.GL_FRAMEBUFFER_UNSUPPORTED -> return FramebufferStatus.UNSUPPORTED
            else -> return FramebufferStatus.UNKNOWN
        }
    }

    fun drawbuffers(attachments: Int) {
        if (attachments < 0 || attachments > 15) {
            throw IllegalArgumentException(
                    "Attachments must be 0-15, was " + attachments)
        }
        GLES30.glDrawBuffers(attachments, attachmentBuffer)
    }

    fun printLogShader(id: Int) {
        val length = readInts { i0 ->
            GLES20.glGetShaderiv(id, GLES20.GL_INFO_LOG_LENGTH, i0)
        }
        if (length > 1) {
            val out = GLES20.glGetShaderInfoLog(id)
            logger.info { "Shader log: $out" }
        }
    }

    fun printLogProgram(id: Int) {
        val length = readInts { i0 ->
            GLES20.glGetProgramiv(id, GLES20.GL_INFO_LOG_LENGTH, i0)
        }
        if (length > 1) {
            val out = GLES20.glGetProgramInfoLog(id)
            logger.info { "Program log: $out" }
        }
    }

    fun compileShader(shader: CompiledShader,
                      properties: Map<String, Expression>) =
            try {
                GLSLGenerator.generate(GLSLGenerator.Version.GLES_300, shader,
                        properties)
            } catch (e: ShaderException) {
                throw IOException(e)
            }

    fun createProgram(vertexSource: String,
                      fragmentSource: String,
                      uniforms: Array<Uniform?>): Pair<Int, IntArray> {
        val vertex = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
        GLES20.glShaderSource(vertex, vertexSource)
        GLES20.glCompileShader(vertex)
        printLogShader(vertex)
        val fragment = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)
        GLES20.glShaderSource(fragment, fragmentSource)
        GLES20.glCompileShader(fragment)
        printLogShader(fragment)
        val program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertex)
        GLES20.glAttachShader(program, fragment)
        GLES20.glLinkProgram(program)
        val status = readInts { i0 ->
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, i0)
        }
        if (status != GLES20.GL_TRUE) {
            logger.error { "Failed to link status bar!" }
            printLogProgram(program)
        }
        val uniformLocations = IntArray(uniforms.size)
        for (i in uniforms.indices) {
            val uniform = uniforms[i]
            if (uniform == null) {
                uniformLocations[i] = -1
            } else {
                uniformLocations[i] = GLES20.glGetUniformLocation(program,
                        uniform.identifier.name)
            }
        }
        GLES20.glDetachShader(program, vertex)
        GLES20.glDetachShader(program, fragment)
        GLES20.glDeleteShader(vertex)
        GLES20.glDeleteShader(fragment)
        return Pair(program, uniformLocations)
    }
}

val buffers = ThreadLocal {
    Buffers(ByteBuffer.allocateDirect(16).order(
            ByteOrder.nativeOrder()).asIntBuffer())
}

class Buffers(val i4at0: IntBuffer) {
    val i3at0 = i4at0.slice(0, 3)
    val i2at0 = i4at0.slice(0, 2)
    val i2at2 = i4at0.slice(2, 4)
    val i1at0 = i4at0.slice(0, 1)
    val i1at1 = i4at0.slice(1, 2)
    val i1at2 = i4at0.slice(2, 3)
    val i1at3 = i4at0.slice(3, 4)
}

private fun IntBuffer.slice(start: Int,
                            end: Int): IntBuffer {
    position(start)
    limit(end)
    val slice = slice()
    clear()
    return slice
}

inline fun <R> intBuffers(block: (IntBuffer) -> R): R {
    val i0 = buffers.get().i1at0
    try {
        return block(i0)
    } finally {
        i0.clear()
    }
}

inline fun <R> intBuffers(block: (IntBuffer, IntBuffer) -> R): R {
    val i0 = buffers.get().i1at0
    val i1 = buffers.get().i1at1
    try {
        return block(i0, i1)
    } finally {
        i0.clear()
        i1.clear()
    }
}

inline fun <R> intBuffers(block: (IntBuffer, IntBuffer, IntBuffer) -> R): R {
    val i0 = buffers.get().i1at0
    val i1 = buffers.get().i1at1
    val i2 = buffers.get().i1at2
    try {
        return block(i0, i1, i2)
    } finally {
        i0.clear()
        i1.clear()
        i2.clear()
    }
}

inline fun <R> intBuffers(block: (IntBuffer, IntBuffer, IntBuffer, IntBuffer) -> R): R {
    val i0 = buffers.get().i1at0
    val i1 = buffers.get().i1at1
    val i2 = buffers.get().i1at2
    val i3 = buffers.get().i1at3
    try {
        return block(i0, i1, i2, i3)
    } finally {
        i0.clear()
        i1.clear()
        i2.clear()
        i3.clear()
    }
}

inline fun <R> intBuffers2(block: (IntBuffer) -> R): R {
    val i0 = buffers.get().i2at0
    try {
        return block(i0)
    } finally {
        i0.clear()
    }
}

inline fun <R> intBuffers2(block: (IntBuffer, IntBuffer) -> R): R {
    val i0 = buffers.get().i2at0
    val i1 = buffers.get().i2at2
    try {
        return block(i0, i1)
    } finally {
        i0.clear()
        i1.clear()
    }
}

inline fun <R> intBuffers3(block: (IntBuffer) -> R): R {
    val i0 = buffers.get().i3at0
    try {
        return block(i0)
    } finally {
        i0.clear()
    }
}

inline fun <R> intBuffers4(block: (IntBuffer) -> R): R {
    val i0 = buffers.get().i4at0
    try {
        return block(i0)
    } finally {
        i0.clear()
    }
}


inline fun <R> intBuffers(v0: Int,
                          block: (IntBuffer) -> R): R {
    intBuffers { i0 ->
        i0.put(0, v0)
        return block(i0)
    }
}

inline fun <R> intBuffers(v0: Int,
                          v1: Int,
                          block: (IntBuffer, IntBuffer) -> R): R {
    intBuffers { i0, i1 ->
        i0.put(0, v0)
        i1.put(0, v1)
        return block(i0, i1)
    }
}

inline fun <R> intBuffers(v0: Int,
                          v1: Int,
                          v2: Int,
                          block: (IntBuffer, IntBuffer, IntBuffer) -> R): R {
    intBuffers { i0, i1, i2 ->
        i0.put(0, v0)
        i1.put(0, v1)
        i2.put(0, v2)
        return block(i0, i1, i2)
    }
}

inline fun <R> intBuffers(v0: Int,
                          v1: Int,
                          v2: Int,
                          v3: Int,
                          block: (IntBuffer, IntBuffer, IntBuffer, IntBuffer) -> R): R {
    intBuffers { i0, i1, i2, i3 ->
        i0.put(0, v0)
        i1.put(0, v1)
        i2.put(0, v2)
        i3.put(0, v3)
        return block(i0, i1, i2, i3)
    }
}

inline fun readInts(block: (IntBuffer) -> Unit) =
        intBuffers { i0 ->
            block(i0)
            i0[0]
        }

inline fun readInts(block: (IntBuffer, IntBuffer) -> Unit) =
        intBuffers { i0, i1 ->
            block(i0, i1)
            Pair(i0[0], i1[0])
        }

inline fun readInts(block: (IntBuffer, IntBuffer, IntBuffer) -> Unit) =
        intBuffers { i0, i1, i2 ->
            block(i0, i1, i2)
            Triple(i0[0], i1[0], i2[0])
        }

internal class CurrentFBO {
    var current: Int = 0
}
