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

@file:Suppress("NOTHING_TO_INLINE")

package org.tobi29.scapes.engine.android.opengles

import android.opengl.GLES20
import android.opengl.GLES30
import org.tobi29.scapes.engine.utils.io.ByteBuffer

const val NOOP = -1

inline fun noop() {}

const val GL_FRAMEBUFFER = GLES20.GL_FRAMEBUFFER
const val GL_COLOR_BUFFER_BIT = GLES20.GL_COLOR_BUFFER_BIT
const val GL_DEPTH_BUFFER_BIT = GLES20.GL_DEPTH_BUFFER_BIT
const val GL_TEXTURE_2D = GLES20.GL_TEXTURE_2D
const val GL_RGB = GLES20.GL_RGB
const val GL_RGBA = GLES20.GL_RGBA
const val GL_RGB16F = GLES30.GL_RGB16F
const val GL_RGBA16F = GLES30.GL_RGBA16F
const val GL_BYTE = GLES20.GL_BYTE
const val GL_SHORT = GLES20.GL_SHORT
const val GL_UNSIGNED_BYTE = GLES20.GL_UNSIGNED_BYTE
const val GL_UNSIGNED_SHORT = GLES20.GL_UNSIGNED_SHORT
const val GL_UNSIGNED_INT = GLES20.GL_UNSIGNED_INT
const val GL_FLOAT = GLES20.GL_FLOAT
const val GL_HALF_FLOAT = GLES30.GL_HALF_FLOAT
const val GL_DEPTH_COMPONENT = GLES20.GL_DEPTH_COMPONENT
const val GL_DEPTH_COMPONENT24 = GLES30.GL_DEPTH_COMPONENT24
const val GL_COLOR_ATTACHMENT0 = GLES20.GL_COLOR_ATTACHMENT0
const val GL_COLOR_ATTACHMENT1 = GLES30.GL_COLOR_ATTACHMENT1
const val GL_COLOR_ATTACHMENT2 = GLES30.GL_COLOR_ATTACHMENT2
const val GL_COLOR_ATTACHMENT3 = GLES30.GL_COLOR_ATTACHMENT3
const val GL_COLOR_ATTACHMENT4 = GLES30.GL_COLOR_ATTACHMENT4
const val GL_COLOR_ATTACHMENT5 = GLES30.GL_COLOR_ATTACHMENT5
const val GL_COLOR_ATTACHMENT6 = GLES30.GL_COLOR_ATTACHMENT6
const val GL_COLOR_ATTACHMENT7 = GLES30.GL_COLOR_ATTACHMENT7
const val GL_COLOR_ATTACHMENT8 = GLES30.GL_COLOR_ATTACHMENT8
const val GL_COLOR_ATTACHMENT9 = GLES30.GL_COLOR_ATTACHMENT9
const val GL_COLOR_ATTACHMENT10 = GLES30.GL_COLOR_ATTACHMENT10
const val GL_COLOR_ATTACHMENT11 = GLES30.GL_COLOR_ATTACHMENT11
const val GL_COLOR_ATTACHMENT12 = GLES30.GL_COLOR_ATTACHMENT12
const val GL_COLOR_ATTACHMENT13 = GLES30.GL_COLOR_ATTACHMENT13
const val GL_COLOR_ATTACHMENT14 = GLES30.GL_COLOR_ATTACHMENT14
const val GL_COLOR_ATTACHMENT15 = GLES30.GL_COLOR_ATTACHMENT15
const val GL_COLOR_ATTACHMENT16 = NOOP
const val GL_COLOR_ATTACHMENT17 = NOOP
const val GL_COLOR_ATTACHMENT18 = NOOP
const val GL_COLOR_ATTACHMENT19 = NOOP
const val GL_COLOR_ATTACHMENT20 = NOOP
const val GL_COLOR_ATTACHMENT21 = NOOP
const val GL_COLOR_ATTACHMENT22 = NOOP
const val GL_COLOR_ATTACHMENT23 = NOOP
const val GL_COLOR_ATTACHMENT24 = NOOP
const val GL_COLOR_ATTACHMENT25 = NOOP
const val GL_COLOR_ATTACHMENT26 = NOOP
const val GL_COLOR_ATTACHMENT27 = NOOP
const val GL_COLOR_ATTACHMENT28 = NOOP
const val GL_COLOR_ATTACHMENT29 = NOOP
const val GL_COLOR_ATTACHMENT30 = NOOP
const val GL_COLOR_ATTACHMENT31 = NOOP
val GL_COLOR_ATTACHMENT = arrayListOf(
        GL_COLOR_ATTACHMENT0,
        GL_COLOR_ATTACHMENT1,
        GL_COLOR_ATTACHMENT2,
        GL_COLOR_ATTACHMENT3,
        GL_COLOR_ATTACHMENT4,
        GL_COLOR_ATTACHMENT5,
        GL_COLOR_ATTACHMENT6,
        GL_COLOR_ATTACHMENT7,
        GL_COLOR_ATTACHMENT8,
        GL_COLOR_ATTACHMENT9,
        GL_COLOR_ATTACHMENT10,
        GL_COLOR_ATTACHMENT11,
        GL_COLOR_ATTACHMENT12,
        GL_COLOR_ATTACHMENT13,
        GL_COLOR_ATTACHMENT14,
        GL_COLOR_ATTACHMENT15,
        GL_COLOR_ATTACHMENT16,
        GL_COLOR_ATTACHMENT17,
        GL_COLOR_ATTACHMENT18,
        GL_COLOR_ATTACHMENT19,
        GL_COLOR_ATTACHMENT20,
        GL_COLOR_ATTACHMENT21,
        GL_COLOR_ATTACHMENT22,
        GL_COLOR_ATTACHMENT23,
        GL_COLOR_ATTACHMENT24,
        GL_COLOR_ATTACHMENT25,
        GL_COLOR_ATTACHMENT26,
        GL_COLOR_ATTACHMENT27,
        GL_COLOR_ATTACHMENT28,
        GL_COLOR_ATTACHMENT29,
        GL_COLOR_ATTACHMENT30,
        GL_COLOR_ATTACHMENT31
)
const val GL_TEXTURE0 = GLES20.GL_TEXTURE0
const val GL_TEXTURE1 = GLES20.GL_TEXTURE1
const val GL_TEXTURE2 = GLES20.GL_TEXTURE2
const val GL_TEXTURE3 = GLES20.GL_TEXTURE3
const val GL_TEXTURE4 = GLES20.GL_TEXTURE4
const val GL_TEXTURE5 = GLES20.GL_TEXTURE5
const val GL_TEXTURE6 = GLES20.GL_TEXTURE6
const val GL_TEXTURE7 = GLES20.GL_TEXTURE7
const val GL_TEXTURE8 = GLES20.GL_TEXTURE8
const val GL_TEXTURE9 = GLES20.GL_TEXTURE9
const val GL_TEXTURE10 = GLES20.GL_TEXTURE10
const val GL_TEXTURE11 = GLES20.GL_TEXTURE11
const val GL_TEXTURE12 = GLES20.GL_TEXTURE12
const val GL_TEXTURE13 = GLES20.GL_TEXTURE13
const val GL_TEXTURE14 = GLES20.GL_TEXTURE14
const val GL_TEXTURE15 = GLES20.GL_TEXTURE15
const val GL_TEXTURE16 = GLES20.GL_TEXTURE16
const val GL_TEXTURE17 = GLES20.GL_TEXTURE17
const val GL_TEXTURE18 = GLES20.GL_TEXTURE18
const val GL_TEXTURE19 = GLES20.GL_TEXTURE19
const val GL_TEXTURE20 = GLES20.GL_TEXTURE20
const val GL_TEXTURE21 = GLES20.GL_TEXTURE21
const val GL_TEXTURE22 = GLES20.GL_TEXTURE22
const val GL_TEXTURE23 = GLES20.GL_TEXTURE23
const val GL_TEXTURE24 = GLES20.GL_TEXTURE24
const val GL_TEXTURE25 = GLES20.GL_TEXTURE25
const val GL_TEXTURE26 = GLES20.GL_TEXTURE26
const val GL_TEXTURE27 = GLES20.GL_TEXTURE27
const val GL_TEXTURE28 = GLES20.GL_TEXTURE28
const val GL_TEXTURE29 = GLES20.GL_TEXTURE29
const val GL_TEXTURE30 = GLES20.GL_TEXTURE30
const val GL_TEXTURE31 = GLES20.GL_TEXTURE31
val GL_TEXTURE = arrayListOf(
        GL_TEXTURE0,
        GL_TEXTURE1,
        GL_TEXTURE2,
        GL_TEXTURE3,
        GL_TEXTURE4,
        GL_TEXTURE5,
        GL_TEXTURE6,
        GL_TEXTURE7,
        GL_TEXTURE8,
        GL_TEXTURE9,
        GL_TEXTURE10,
        GL_TEXTURE11,
        GL_TEXTURE12,
        GL_TEXTURE13,
        GL_TEXTURE14,
        GL_TEXTURE15,
        GL_TEXTURE16,
        GL_TEXTURE17,
        GL_TEXTURE18,
        GL_TEXTURE19,
        GL_TEXTURE20,
        GL_TEXTURE21,
        GL_TEXTURE22,
        GL_TEXTURE23,
        GL_TEXTURE24,
        GL_TEXTURE25,
        GL_TEXTURE26,
        GL_TEXTURE27,
        GL_TEXTURE28,
        GL_TEXTURE29,
        GL_TEXTURE30,
        GL_TEXTURE31
)
const val GL_DEPTH_ATTACHMENT = GLES20.GL_DEPTH_ATTACHMENT
const val GL_TEXTURE_MAG_FILTER = GLES20.GL_TEXTURE_MAG_FILTER
const val GL_TEXTURE_MIN_FILTER = GLES20.GL_TEXTURE_MIN_FILTER
const val GL_TEXTURE_WRAP_S = GLES20.GL_TEXTURE_WRAP_S
const val GL_TEXTURE_WRAP_T = GLES20.GL_TEXTURE_WRAP_T
const val GL_REPEAT = GLES20.GL_REPEAT
const val GL_NEAREST = GLES20.GL_NEAREST
const val GL_LINEAR = GLES20.GL_LINEAR
const val GL_CLAMP_TO_EDGE = GLES20.GL_CLAMP_TO_EDGE
const val GL_NEAREST_MIPMAP_LINEAR = GLES20.GL_NEAREST_MIPMAP_LINEAR
const val GL_LINEAR_MIPMAP_LINEAR = GLES20.GL_LINEAR_MIPMAP_LINEAR
const val GL_TEXTURE_MAX_LEVEL = GLES30.GL_TEXTURE_MAX_LEVEL
const val GL_ARRAY_BUFFER = GLES20.GL_ARRAY_BUFFER
const val GL_ELEMENT_ARRAY_BUFFER = GLES20.GL_ELEMENT_ARRAY_BUFFER
const val GL_STATIC_DRAW = GLES20.GL_STATIC_DRAW
const val GL_STREAM_DRAW = GLES20.GL_STREAM_DRAW
const val GL_FRONT = GLES20.GL_FRONT
const val GL_FRONT_AND_BACK = GLES20.GL_FRONT_AND_BACK
const val GL_VIEWPORT = GLES20.GL_VIEWPORT
const val GL_BLEND = GLES20.GL_BLEND
const val GL_SRC_ALPHA = GLES20.GL_SRC_ALPHA
const val GL_ONE_MINUS_SRC_ALPHA = GLES20.GL_ONE_MINUS_SRC_ALPHA
const val GL_DST_ALPHA = GLES20.GL_DST_ALPHA
const val GL_ONE_MINUS_DST_COLOR = GLES20.GL_ONE_MINUS_DST_COLOR
const val GL_ONE_MINUS_SRC_COLOR = GLES20.GL_ONE_MINUS_SRC_COLOR
const val GL_LINE = NOOP
const val GL_SCISSOR_TEST = GLES20.GL_SCISSOR_TEST
const val GL_DEPTH_TEST = GLES20.GL_DEPTH_TEST
const val GL_CULL_FACE = GLES20.GL_CULL_FACE
const val GL_LEQUAL = GLES20.GL_LEQUAL
const val GL_FILL = NOOP
const val GL_NO_ERROR = GLES20.GL_NO_ERROR
const val GL_INVALID_ENUM = GLES20.GL_INVALID_ENUM
const val GL_INVALID_VALUE = GLES20.GL_INVALID_VALUE
const val GL_INVALID_OPERATION = GLES20.GL_INVALID_OPERATION
const val GL_STACK_OVERFLOW = NOOP
const val GL_STACK_UNDERFLOW = NOOP
const val GL_OUT_OF_MEMORY = GLES20.GL_OUT_OF_MEMORY
const val GL_INVALID_FRAMEBUFFER_OPERATION = GLES20.GL_INVALID_FRAMEBUFFER_OPERATION
const val GL_TABLE_TOO_LARGE = NOOP

inline fun glBindFramebuffer(target: Int,
                             framebuffer: Int) =
        GLES20.glBindFramebuffer(target, framebuffer)

inline fun glGenFramebuffers() =
        readInts { i0 -> GLES20.glGenFramebuffers(1, i0) }

inline fun glDeleteFramebuffers(framebuffer: Int) =
        intBuffers(framebuffer) { i0 -> GLES20.glDeleteFramebuffers(1, i0) }

inline fun glClear(mask: Int) =
        GLES20.glClear(mask)

inline fun glClearColor(red: Float,
                        green: Float,
                        blue: Float,
                        alpha: Float) =
        GLES20.glClearColor(red, green, blue, alpha)

inline fun glDeleteProgram(program: Int) =
        GLES20.glDeleteProgram(program)

inline fun glUseProgram(program: Int) =
        GLES20.glUseProgram(program)

inline fun glGenTextures() =
        readInts { i0 -> GLES20.glGenTextures(1, i0) }

inline fun glBindTexture(target: Int,
                         texture: Int) =
        GLES20.glBindTexture(target, texture)

inline fun glDeleteTextures(texture: Int) =
        intBuffers(texture) { i0 -> GLES20.glDeleteTextures(1, i0) }


inline fun glTexImage2D(target: Int,
                        level: Int,
                        internalformat: Int,
                        width: Int,
                        height: Int,
                        border: Int,
                        format: Int,
                        type: Int,
                        pixels: ByteBuffer?) =
        GLES20.glTexImage2D(target, level, internalformat, width, height,
                border,
                format, type, pixels)

inline fun glFramebufferTexture2D(target: Int,
                                  attachment: Int,
                                  textarget: Int,
                                  texture: Int,
                                  level: Int) =
        GLES20.glFramebufferTexture2D(target, attachment, textarget, texture,
                level)

inline fun glTexParameteri(target: Int,
                           pname: Int,
                           param: Int) =
        GLES20.glTexParameteri(target, pname, param)

inline fun glBindVertexArray(array: Int) =
        GLES30.glBindVertexArray(array)

inline fun glGenVertexArrays() =
        readInts { i0 -> GLES30.glGenVertexArrays(1, i0) }

inline fun glDeleteVertexArrays(array: Int) =
        intBuffers(array) { i0 -> GLES30.glDeleteVertexArrays(1, i0) }

inline fun glDrawArrays(mode: Int,
                        first: Int,
                        count: Int) =
        GLES20.glDrawArrays(mode, first, count)

inline fun glDrawArraysInstanced(mode: Int,
                                 first: Int,
                                 count: Int,
                                 primcount: Int) =
        GLES30.glDrawArraysInstanced(mode, first, count, primcount)

inline fun glDrawElements(mode: Int,
                          count: Int,
                          type: Int,
                          indices: Int) =
        GLES20.glDrawElements(mode, count, type, indices)

inline fun glGenBuffers() =
        readInts { i0 -> GLES20.glGenBuffers(1, i0) }

inline fun glDeleteBuffers(buffer: Int) =
        intBuffers(buffer) { i0 -> GLES20.glDeleteBuffers(1, i0) }

inline fun glBindBuffer(target: Int,
                        buffer: Int) =
        GLES20.glBindBuffer(target, buffer)

inline fun glBufferData(target: Int,
                        size: Int,
                        usage: Int) =
        GLES20.glBufferData(target, size, null, usage)

inline fun glBufferData(target: Int,
                        data: ByteBuffer,
                        usage: Int) =
        GLES20.glBufferData(target, data.remaining(), data, usage)

inline fun glBufferSubData(target: Int,
                           offset: Int,
                           data: ByteBuffer) =
        GLES20.glBufferSubData(target, offset, data.remaining(), data)

inline fun glVertexAttribDivisor(index: Int,
                                 divisor: Int) =
        GLES30.glVertexAttribDivisor(index, divisor)

inline fun glEnableVertexAttribArray(index: Int) =
        GLES20.glEnableVertexAttribArray(index)

inline fun glVertexAttribPointer(index: Int,
                                 size: Int,
                                 type: Int,
                                 normalized: Boolean,
                                 stride: Int,
                                 pointer: Int) =
        GLES20.glVertexAttribPointer(index, size, type, normalized, stride,
                pointer)

inline fun glVertexAttribIPointer(index: Int,
                                  size: Int,
                                  type: Int,
                                  stride: Int,
                                  pointer: Int) =
        GLES30.glVertexAttribIPointer(index, size, type, stride, pointer)

inline fun glEnable(target: Int) =
        GLES20.glEnable(target)

inline fun glDisable(target: Int) =
        GLES20.glDisable(target)

inline fun glDepthMask(flag: Boolean) =
        GLES20.glDepthMask(flag)

inline fun glPolygonMode(face: Int,
                         mode: Int) =
        noop()

inline fun glDepthFunc(func: Int) =
        GLES20.glDepthFunc(func)

inline fun glScissor(x: Int,
                     y: Int,
                     width: Int,
                     height: Int) =
        GLES20.glScissor(x, y, width, height)

inline fun glViewport(x: Int,
                      y: Int,
                      w: Int,
                      h: Int) =
        GLES20.glViewport(x, y, w, h)

inline fun glGetIntegerv(pname: Int,
                         params: IntArray) =
        intBuffers4 { i0 ->
            GLES20.glGetIntegerv(pname, i0)
            i0.get(params)
            params
        }

inline fun glReadBuffer(src: Int) =
        GLES30.glReadBuffer(src)

inline fun glReadPixels(x: Int,
                        y: Int,
                        width: Int,
                        height: Int,
                        format: Int,
                        type: Int,
                        pixels: ByteBuffer) =
        GLES20.glReadPixels(x, y, width, height, format, type, pixels)

inline fun glBlendFunc(sfactor: Int,
                       dfactor: Int) =
        GLES20.glBlendFunc(sfactor, dfactor)

inline fun glTexSubImage2D(target: Int,
                           level: Int,
                           xoffset: Int,
                           yoffset: Int,
                           width: Int,
                           height: Int,
                           format: Int,
                           type: Int,
                           pixels: ByteBuffer) =
        GLES20.glTexSubImage2D(target, level, xoffset, yoffset, width, height,
                format, type, pixels)

inline fun glGetTexImage(tex: Int,
                         level: Int,
                         format: Int,
                         type: Int,
                         pixels: ByteBuffer) =
        noop()

inline fun glGetError() =
        GLES20.glGetError()

inline fun glActiveTexture(texture: Int) =
        GLES20.glActiveTexture(texture)

inline fun glUniform1f(location: Int,
                       v0: Float) =
        GLES20.glUniform1f(location, v0)

inline fun glUniform2f(location: Int,
                       v0: Float,
                       v1: Float) =
        GLES20.glUniform2f(location, v0, v1)

inline fun glUniform3f(location: Int,
                       v0: Float,
                       v1: Float,
                       v2: Float) =
        GLES20.glUniform3f(location, v0, v1, v2)

inline fun glUniform4f(location: Int,
                       v0: Float,
                       v1: Float,
                       v2: Float,
                       v3: Float) =
        GLES20.glUniform4f(location, v0, v1, v2, v3)

inline fun glUniform1i(location: Int,
                       v0: Int) =
        GLES20.glUniform1i(location, v0)

inline fun glUniform2i(location: Int,
                       v0: Int,
                       v1: Int) =
        GLES20.glUniform2i(location, v0, v1)

inline fun glUniform3i(location: Int,
                       v0: Int,
                       v1: Int,
                       v2: Int) =
        GLES20.glUniform3i(location, v0, v1, v2)

inline fun glUniform4i(location: Int,
                       v0: Int,
                       v1: Int,
                       v2: Int,
                       v3: Int) =
        GLES20.glUniform4i(location, v0, v1, v2, v3)

inline fun glUniform1fv(location: Int,
                        value: FloatArray) =
        GLES20.glUniform1fv(location, value.size, value, 0)

inline fun glUniform2fv(location: Int,
                        value: FloatArray) =
        GLES20.glUniform2fv(location, value.size shr 1, value, 0)

inline fun glUniform3fv(location: Int,
                        value: FloatArray) =
        GLES20.glUniform3fv(location, value.size / 3, value, 0)

inline fun glUniform4fv(location: Int,
                        value: FloatArray) =
        GLES20.glUniform4fv(location, value.size shr 2, value, 0)

inline fun glUniform1iv(location: Int,
                        value: IntArray) =
        GLES20.glUniform1iv(location, value.size, value, 0)

inline fun glUniform2iv(location: Int,
                        value: IntArray) =
        GLES20.glUniform2iv(location, value.size shr 1, value, 0)

inline fun glUniform3iv(location: Int,
                        value: IntArray) =
        GLES20.glUniform3iv(location, value.size / 3, value, 0)

inline fun glUniform4iv(location: Int,
                        value: IntArray) =
        GLES20.glUniform4iv(location, value.size shr 2, value, 0)

inline fun glUniformMatrix2fv(location: Int,
                              transpose: Boolean,
                              value: FloatArray) =
        GLES20.glUniformMatrix2fv(location, value.size shr 2, transpose, value,
                0)

inline fun glUniformMatrix3fv(location: Int,
                              transpose: Boolean,
                              value: FloatArray) =
        GLES20.glUniformMatrix3fv(location, value.size / 9, transpose, value, 0)

inline fun glUniformMatrix4fv(location: Int,
                              transpose: Boolean,
                              value: FloatArray) =
        GLES20.glUniformMatrix4fv(location, value.size shr 4, transpose, value,
                0)

inline fun glVertexAttrib1f(location: Int,
                            v0: Float) =
        GLES20.glVertexAttrib1f(location, v0)

inline fun glVertexAttrib2f(location: Int,
                            v0: Float,
                            v1: Float) =
        GLES20.glVertexAttrib2f(location, v0, v1)

inline fun glVertexAttrib3f(location: Int,
                            v0: Float,
                            v1: Float,
                            v2: Float) =
        GLES20.glVertexAttrib3f(location, v0, v1, v2)

inline fun glVertexAttrib4f(location: Int,
                            v0: Float,
                            v1: Float,
                            v2: Float,
                            v3: Float) =
        GLES20.glVertexAttrib4f(location, v0, v1, v2, v3)

inline fun glVertexAttrib1fv(location: Int,
                             value: FloatArray) =
        GLES20.glVertexAttrib1fv(location, value, 0)

inline fun glVertexAttrib2fv(location: Int,
                             value: FloatArray) =
        GLES20.glVertexAttrib2fv(location, value, 0)

inline fun glVertexAttrib3fv(location: Int,
                             value: FloatArray) =
        GLES20.glVertexAttrib3fv(location, value, 0)

inline fun glVertexAttrib4fv(location: Int,
                             value: FloatArray) =
        GLES20.glVertexAttrib4fv(location, value, 0)
