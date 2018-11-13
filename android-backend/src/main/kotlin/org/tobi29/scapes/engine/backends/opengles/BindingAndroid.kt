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

@file:Suppress("NOTHING_TO_INLINE", "unused")

package org.tobi29.scapes.engine.backends.opengles

import android.opengl.GLES20
import android.opengl.GLES30
import org.tobi29.arrays.Bytes
import org.tobi29.arrays.BytesRO
import org.tobi29.io.*

actual inline fun GLESHandle.byteView(capacity: Int): ByteViewE =
    ByteBufferNative(capacity).viewE

actual inline fun GLESHandle.glBindFramebuffer(
    target: Int,
    framebuffer: Int
) = GLES20.glBindFramebuffer(target, framebuffer)

actual inline fun GLESHandle.glGenFramebuffers() =
    readInts { i0 -> GLES20.glGenFramebuffers(1, i0) }

actual inline fun GLESHandle.glDeleteFramebuffers(framebuffer: Int) =
    intBuffers(framebuffer) { i0 -> GLES20.glDeleteFramebuffers(1, i0) }

actual inline fun GLESHandle.glClear(mask: Int) = GLES20.glClear(mask)

actual inline fun GLESHandle.glClearColor(
    red: Float,
    green: Float,
    blue: Float,
    alpha: Float
) = GLES20.glClearColor(red, green, blue, alpha)

actual inline fun GLESHandle.glDeleteProgram(program: Int) =
    GLES20.glDeleteProgram(program)

actual inline fun GLESHandle.glUseProgram(program: Int) =
    GLES20.glUseProgram(program)

actual inline fun GLESHandle.glGenTextures() =
    readInts { i0 -> GLES20.glGenTextures(1, i0) }

actual inline fun GLESHandle.glBindTexture(
    target: Int,
    texture: Int
) = GLES20.glBindTexture(target, texture)

actual inline fun GLESHandle.glDeleteTextures(texture: Int) =
    intBuffers(texture) { i0 -> GLES20.glDeleteTextures(1, i0) }

actual inline fun GLESHandle.glTexImage2D(
    target: GLEnum,
    level: Int,
    internalformat: GLEnum,
    width: Int,
    height: Int,
    border: Int,
    format: GLEnum,
    type: GLEnum,
    pixels: BytesRO?
) = GLES20.glTexImage2D(
    target, level, internalformat, width, height, border,
    format, type, pixels?.readAsNativeByteBuffer()
)

actual inline fun GLESHandle.glFramebufferTexture2D(
    target: Int,
    attachment: Int,
    textarget: Int,
    texture: Int,
    level: Int
) = GLES20.glFramebufferTexture2D(target, attachment, textarget, texture, level)

actual inline fun GLESHandle.glTexParameteri(
    target: Int,
    pname: Int,
    param: Int
) = GLES20.glTexParameteri(target, pname, param)

actual inline fun GLESHandle.glBindVertexArray(array: Int) =
    GLES30.glBindVertexArray(array)

actual inline fun GLESHandle.glGenVertexArrays() =
    readInts { i0 -> GLES30.glGenVertexArrays(1, i0) }

actual inline fun GLESHandle.glDeleteVertexArrays(array: Int) =
    intBuffers(array) { i0 -> GLES30.glDeleteVertexArrays(1, i0) }

actual inline fun GLESHandle.glDrawArrays(
    mode: Int,
    first: Int,
    count: Int
) = GLES20.glDrawArrays(mode, first, count)

actual inline fun GLESHandle.glDrawArraysInstanced(
    mode: Int,
    first: Int,
    count: Int,
    primcount: Int
) = GLES30.glDrawArraysInstanced(mode, first, count, primcount)

actual inline fun GLESHandle.glDrawElements(
    mode: Int,
    count: Int,
    type: Int,
    indices: Int
) = GLES20.glDrawElements(mode, count, type, indices)

actual inline fun GLESHandle.glGenBuffers() =
    readInts { i0 -> GLES20.glGenBuffers(1, i0) }

actual inline fun GLESHandle.glDeleteBuffers(buffer: Int) =
    intBuffers(buffer) { i0 -> GLES20.glDeleteBuffers(1, i0) }

actual inline fun GLESHandle.glBindBuffer(
    target: Int,
    buffer: Int
) = GLES20.glBindBuffer(target, buffer)

actual inline fun GLESHandle.glBufferData(
    target: Int,
    size: Int,
    usage: Int
) = GLES20.glBufferData(target, size, null, usage)

actual inline fun GLESHandle.glBufferData(
    target: Int,
    data: BytesRO,
    usage: Int
) = GLES20.glBufferData(target, data.size, data.readAsNativeByteBuffer(), usage)

actual inline fun GLESHandle.glBufferSubData(
    target: Int,
    offset: Int,
    data: BytesRO
) = GLES20.glBufferSubData(
    target, offset, data.size,
    data.readAsNativeByteBuffer()
)

actual inline fun GLESHandle.glVertexAttribDivisor(
    index: Int,
    divisor: Int
) = GLES30.glVertexAttribDivisor(index, divisor)

actual inline fun GLESHandle.glEnableVertexAttribArray(index: Int) =
    GLES20.glEnableVertexAttribArray(index)

actual inline fun GLESHandle.glVertexAttribPointer(
    index: Int,
    size: Int,
    type: Int,
    normalized: Boolean,
    stride: Int,
    pointer: Int
) = GLES20.glVertexAttribPointer(index, size, type, normalized, stride, pointer)

actual inline fun GLESHandle.glVertexAttribIPointer(
    index: Int,
    size: Int,
    type: Int,
    stride: Int,
    pointer: Int
) = GLES30.glVertexAttribIPointer(index, size, type, stride, pointer)

actual inline fun GLESHandle.glEnable(target: Int) = GLES20.glEnable(target)

actual inline fun GLESHandle.glDisable(target: Int) = GLES20.glDisable(target)

actual inline fun GLESHandle.glDepthMask(flag: Boolean) = GLES20.glDepthMask(
    flag
)

actual inline fun GLESHandle.glDepthFunc(func: Int) = GLES20.glDepthFunc(func)

actual inline fun GLESHandle.glScissor(
    x: Int,
    y: Int,
    width: Int,
    height: Int
) = GLES20.glScissor(x, y, width, height)

actual inline fun GLESHandle.glViewport(
    x: Int,
    y: Int,
    w: Int,
    h: Int
) = GLES20.glViewport(x, y, w, h)

actual inline fun GLESHandle.glGetIntegerv(
    pname: Int,
    params: IntArray
) = GLES20.glGetIntegerv(pname, params, 0)

actual inline fun GLESHandle.glReadBuffer(src: Int) = GLES30.glReadBuffer(src)

actual inline fun GLESHandle.glReadPixels(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    format: Int,
    type: Int,
    pixels: Bytes
) = pixels.mutateAsNativeByteBuffer {
    GLES20.glReadPixels(x, y, width, height, format, type, it)
}

actual inline fun GLESHandle.glBlendFunc(
    sfactor: Int,
    dfactor: Int
) = GLES20.glBlendFunc(sfactor, dfactor)

actual inline fun GLESHandle.glTexSubImage2D(
    target: Int,
    level: Int,
    xoffset: Int,
    yoffset: Int,
    width: Int,
    height: Int,
    format: Int,
    type: Int,
    pixels: BytesRO
) = GLES20.glTexSubImage2D(
    target, level, xoffset, yoffset, width, height,
    format, type, pixels.readAsNativeByteBuffer()
)

actual inline fun GLESHandle.glGetError() = GLES20.glGetError()

actual inline fun GLESHandle.glActiveTexture(texture: Int) =
    GLES20.glActiveTexture(texture)

actual inline fun GLESHandle.glUniform1f(
    location: Int,
    v0: Float
) = GLES20.glUniform1f(location, v0)

actual inline fun GLESHandle.glUniform2f(
    location: Int,
    v0: Float,
    v1: Float
) = GLES20.glUniform2f(location, v0, v1)

actual inline fun GLESHandle.glUniform3f(
    location: Int,
    v0: Float,
    v1: Float,
    v2: Float
) = GLES20.glUniform3f(location, v0, v1, v2)

actual inline fun GLESHandle.glUniform4f(
    location: Int,
    v0: Float,
    v1: Float,
    v2: Float,
    v3: Float
) = GLES20.glUniform4f(location, v0, v1, v2, v3)

actual inline fun GLESHandle.glUniform1i(
    location: Int,
    v0: Int
) = GLES20.glUniform1i(location, v0)

actual inline fun GLESHandle.glUniform2i(
    location: Int,
    v0: Int,
    v1: Int
) = GLES20.glUniform2i(location, v0, v1)

actual inline fun GLESHandle.glUniform3i(
    location: Int,
    v0: Int,
    v1: Int,
    v2: Int
) = GLES20.glUniform3i(location, v0, v1, v2)

actual inline fun GLESHandle.glUniform4i(
    location: Int,
    v0: Int,
    v1: Int,
    v2: Int,
    v3: Int
) = GLES20.glUniform4i(location, v0, v1, v2, v3)

actual inline fun GLESHandle.glUniform1fv(
    location: Int,
    value: FloatArray
) = GLES20.glUniform1fv(location, value.size, value, 0)

actual inline fun GLESHandle.glUniform2fv(
    location: Int,
    value: FloatArray
) = GLES20.glUniform2fv(location, value.size shr 1, value, 0)

actual inline fun GLESHandle.glUniform3fv(
    location: Int,
    value: FloatArray
) = GLES20.glUniform3fv(location, value.size / 3, value, 0)

actual inline fun GLESHandle.glUniform4fv(
    location: Int,
    value: FloatArray
) = GLES20.glUniform4fv(location, value.size shr 2, value, 0)

actual inline fun GLESHandle.glUniform1iv(
    location: Int,
    value: IntArray
) = GLES20.glUniform1iv(location, value.size, value, 0)

actual inline fun GLESHandle.glUniform2iv(
    location: Int,
    value: IntArray
) =
    GLES20.glUniform2iv(location, value.size shr 1, value, 0)

actual inline fun GLESHandle.glUniform3iv(
    location: Int,
    value: IntArray
) = GLES20.glUniform3iv(location, value.size / 3, value, 0)

actual inline fun GLESHandle.glUniform4iv(
    location: Int,
    value: IntArray
) = GLES20.glUniform4iv(location, value.size shr 2, value, 0)

actual inline fun GLESHandle.glUniformMatrix2fv(
    location: Int,
    transpose: Boolean,
    value: FloatArray
) = GLES20.glUniformMatrix2fv(location, value.size shr 2, transpose, value, 0)

actual inline fun GLESHandle.glUniformMatrix3fv(
    location: Int,
    transpose: Boolean,
    value: FloatArray
) = GLES20.glUniformMatrix3fv(location, value.size / 9, transpose, value, 0)

actual inline fun GLESHandle.glUniformMatrix4fv(
    location: Int,
    transpose: Boolean,
    value: FloatArray
) = GLES20.glUniformMatrix4fv(location, value.size shr 4, transpose, value, 0)

actual inline fun GLESHandle.glVertexAttrib1f(
    location: Int,
    v0: Float
) = GLES20.glVertexAttrib1f(location, v0)

actual inline fun GLESHandle.glVertexAttrib2f(
    location: Int,
    v0: Float,
    v1: Float
) = GLES20.glVertexAttrib2f(location, v0, v1)

actual inline fun GLESHandle.glVertexAttrib3f(
    location: Int,
    v0: Float,
    v1: Float,
    v2: Float
) = GLES20.glVertexAttrib3f(location, v0, v1, v2)

actual inline fun GLESHandle.glVertexAttrib4f(
    location: Int,
    v0: Float,
    v1: Float,
    v2: Float,
    v3: Float
) = GLES20.glVertexAttrib4f(location, v0, v1, v2, v3)

actual inline fun GLESHandle.glVertexAttrib1fv(
    location: Int,
    value: FloatArray
) = GLES20.glVertexAttrib1fv(location, value, 0)

actual inline fun GLESHandle.glVertexAttrib2fv(
    location: Int,
    value: FloatArray
) = GLES20.glVertexAttrib2fv(location, value, 0)

actual inline fun GLESHandle.glVertexAttrib3fv(
    location: Int,
    value: FloatArray
) = GLES20.glVertexAttrib3fv(location, value, 0)

actual inline fun GLESHandle.glVertexAttrib4fv(
    location: Int,
    value: FloatArray
) = GLES20.glVertexAttrib4fv(location, value, 0)

actual inline fun GLESHandle.glCheckFramebufferStatus(
    target: Int
) = GLES20.glCheckFramebufferStatus(target)

actual inline fun GLESHandle.glDrawBuffers(
    bufs: IntArray
) = GLES30.glDrawBuffers(bufs.size, bufs, 0)

actual inline fun GLESHandle.glGetShaderInfoLog(
    shader: Int
) = GLES20.glGetShaderInfoLog(shader)

actual inline fun GLESHandle.glGetProgramInfoLog(
    program: Int
) = GLES20.glGetProgramInfoLog(program)

actual inline fun GLESHandle.glGetProgramb(
    program: GLProgram,
    pname: GLEnum
): Boolean = readInts { i0 ->
    GLES20.glGetProgramiv(program, pname, i0)
} == GLES20.GL_TRUE

actual inline fun GLESHandle.glShaderSource(
    shader: Int,
    string: String
) = GLES20.glShaderSource(shader, string)

actual inline fun GLESHandle.glCompileShader(
    shader: Int
) = GLES20.glCompileShader(shader)

actual inline fun GLESHandle.glAttachShader(
    program: Int,
    shader: Int
) = GLES20.glAttachShader(program, shader)

actual inline fun GLESHandle.glLinkProgram(
    program: Int
) = GLES20.glLinkProgram(program)

actual inline fun GLESHandle.glCreateShader(
    type: Int
) = GLES20.glCreateShader(type)

actual inline fun GLESHandle.glCreateProgram(
) = GLES20.glCreateProgram()

actual inline fun GLESHandle.glGetUniformLocation(
    program: Int,
    name: String
) = GLES20.glGetUniformLocation(program, name)

actual inline fun GLESHandle.glDeleteShader(
    shader: Int
) = GLES20.glDeleteShader(shader)

actual inline fun GLESHandle.glDetachShader(
    program: Int,
    shader: Int
) = GLES20.glDetachShader(program, shader)
