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

import org.tobi29.scapes.engine.graphics.GL
import org.tobi29.scapes.engine.graphics.ModelHybrid
import org.tobi29.scapes.engine.graphics.RenderType
import org.tobi29.scapes.engine.graphics.Shader
import org.tobi29.scapes.engine.utils.assert
import org.tobi29.scapes.engine.utils.io.ByteBuffer

internal class VAOHybrid(private val vbo1: VBO,
                         private val vbo2: VBO,
                         private val renderType: RenderType) : VAO(
        vbo1.engine), ModelHybrid {
    private var arrayID = 0

    override fun render(gl: GL,
                        shader: Shader): Boolean {
        throw UnsupportedOperationException(
                "Cannot render hybrid VAO without length parameter")
    }

    override fun render(gl: GL,
                        shader: Shader,
                        length: Int): Boolean {
        if (!ensureStored(gl)) {
            return false
        }
        gl.check()
        shader(gl, shader)
        glBindVertexArray(arrayID)
        glDrawArrays(GLUtils.renderType(renderType), 0, length)
        return true
    }

    override fun renderInstanced(gl: GL,
                                 shader: Shader,
                                 count: Int): Boolean {
        throw UnsupportedOperationException(
                "Cannot render hybrid VAO without length parameter")
    }

    override fun renderInstanced(gl: GL,
                                 shader: Shader,
                                 length: Int,
                                 count: Int): Boolean {
        if (!ensureStored(gl)) {
            return false
        }
        gl.check()
        shader(gl, shader)
        glBindVertexArray(arrayID)
        glDrawArraysInstanced(GLUtils.renderType(renderType), 0, length, count)
        return true
    }

    override fun store(gl: GL): Boolean {
        assert { !isStored }
        if (!vbo1.canStore()) {
            return false
        }
        if (!vbo2.canStore()) {
            return false
        }
        isStored = true
        gl.check()
        arrayID = glGenVertexArrays()
        glBindVertexArray(arrayID)
        vbo1.store(gl, weak)
        vbo2.store(gl, weak)
        detach = gl.vaoTracker.attach(this)
        return true
    }

    override fun dispose(gl: GL?) {
        if (!isStored) {
            return
        }
        if (gl != null) {
            gl.check()
            vbo1.dispose(gl)
            vbo2.dispose(gl)
            glDeleteVertexArrays(arrayID)
        }
        isStored = false
        detach?.invoke()
        detach = null
        markAsDisposed = false
        vbo1.reset()
        vbo2.reset()
    }

    override fun strideStream(): Int {
        return vbo2.stride()
    }

    override fun bufferStream(gl: GL,
                              buffer: ByteBuffer) {
        vbo2.replaceBuffer(gl, buffer)
    }
}
