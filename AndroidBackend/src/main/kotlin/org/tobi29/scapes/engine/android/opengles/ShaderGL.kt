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
import org.tobi29.scapes.engine.graphics.Shader
import org.tobi29.scapes.engine.graphics.ShaderCompileInformation
import org.tobi29.scapes.engine.utils.IOException
import org.tobi29.scapes.engine.utils.assert
import org.tobi29.scapes.engine.utils.logging.KLogging
import org.tobi29.scapes.engine.utils.shader.CompiledShader

internal class ShaderGL(private val shader: CompiledShader,
                        private val information: ShaderCompileInformation) : Shader {
    override var isStored = false
    private var valid = false
    private var markAsDisposed = false
    private var uniformLocations: IntArray? = null
    private var program = 0
    private var used: Long = 0
    private var detach: (() -> Unit)? = null

    override fun ensureStored(gl: GL): Boolean {
        if (!isStored) {
            store(gl)
        }
        used = gl.timestamp
        return valid
    }

    override fun ensureDisposed(gl: GL) {
        if (isStored) {
            dispose(gl)
        }
    }

    override fun isUsed(time: Long) =
            time - used < 1000000000L && !markAsDisposed

    override fun dispose(gl: GL?) {
        if (!isStored) {
            return
        }
        if (gl != null) {
            gl.check()
            glDeleteProgram(program)
        }
        isStored = false
        detach?.invoke()
        detach = null
        valid = false
        markAsDisposed = false
    }

    override fun activate(gl: GL) {
        if (!ensureStored(gl)) {
            return
        }
        gl.check()
        glUseProgram(program)
    }

    override fun updateUniforms(gl: GL) {
        gl.check()
    }

    override fun uniformLocation(uniform: Int): Int {
        uniformLocations?.let { return it[uniform] }
        throw IllegalStateException("Shader not stored")
    }

    override fun setUniform1f(gl: GL,
                              uniform: Int,
                              v0: Float) {
        activate(gl)
        glUniform1f(uniformLocation(uniform), v0)
    }

    override fun setUniform2f(gl: GL,
                              uniform: Int,
                              v0: Float,
                              v1: Float) {
        activate(gl)
        glUniform2f(uniformLocation(uniform), v0, v1)
    }

    override fun setUniform3f(gl: GL,
                              uniform: Int,
                              v0: Float,
                              v1: Float,
                              v2: Float) {
        activate(gl)
        glUniform3f(uniformLocation(uniform), v0, v1, v2)
    }

    override fun setUniform4f(gl: GL,
                              uniform: Int,
                              v0: Float,
                              v1: Float,
                              v2: Float,
                              v3: Float) {
        activate(gl)
        glUniform4f(uniformLocation(uniform), v0, v1, v2, v3)
    }

    override fun setUniform1i(gl: GL,
                              uniform: Int,
                              v0: Int) {
        activate(gl)
        glUniform1i(uniformLocation(uniform), v0)
    }

    override fun setUniform2i(gl: GL,
                              uniform: Int,
                              v0: Int,
                              v1: Int) {
        activate(gl)
        glUniform2i(uniformLocation(uniform), v0, v1)
    }

    override fun setUniform3i(gl: GL,
                              uniform: Int,
                              v0: Int,
                              v1: Int,
                              v2: Int) {
        activate(gl)
        glUniform3i(uniformLocation(uniform), v0, v1, v2)
    }

    override fun setUniform4i(gl: GL,
                              uniform: Int,
                              v0: Int,
                              v1: Int,
                              v2: Int,
                              v3: Int) {
        activate(gl)
        glUniform4i(uniformLocation(uniform), v0, v1, v2, v3)
    }

    override fun setUniform1(gl: GL,
                             uniform: Int,
                             values: FloatArray) {
        activate(gl)
        glUniform1fv(uniformLocation(uniform), values)
    }

    override fun setUniform2(gl: GL,
                             uniform: Int,
                             values: FloatArray) {
        activate(gl)
        glUniform2fv(uniformLocation(uniform), values)
    }

    override fun setUniform3(gl: GL,
                             uniform: Int,
                             values: FloatArray) {
        activate(gl)
        glUniform3fv(uniformLocation(uniform), values)
    }

    override fun setUniform4(gl: GL,
                             uniform: Int,
                             values: FloatArray) {
        activate(gl)
        glUniform4fv(uniformLocation(uniform), values)
    }

    override fun setUniform1(gl: GL,
                             uniform: Int,
                             values: IntArray) {
        activate(gl)
        glUniform1iv(uniformLocation(uniform), values)
    }

    override fun setUniform2(gl: GL,
                             uniform: Int,
                             values: IntArray) {
        activate(gl)
        glUniform2iv(uniformLocation(uniform), values)
    }

    override fun setUniform3(gl: GL,
                             uniform: Int,
                             values: IntArray) {
        activate(gl)
        glUniform3iv(uniformLocation(uniform), values)
    }

    override fun setUniform4(gl: GL,
                             uniform: Int,
                             values: IntArray) {
        activate(gl)
        glUniform4iv(uniformLocation(uniform), values)
    }

    override fun setUniformMatrix2(gl: GL,
                                   uniform: Int,
                                   transpose: Boolean,
                                   matrices: FloatArray) {
        activate(gl)
        glUniformMatrix2fv(uniformLocation(uniform), transpose, matrices)
    }

    override fun setUniformMatrix3(gl: GL,
                                   uniform: Int,
                                   transpose: Boolean,
                                   matrices: FloatArray) {
        activate(gl)
        glUniformMatrix3fv(uniformLocation(uniform), transpose, matrices)
    }

    override fun setUniformMatrix4(gl: GL,
                                   uniform: Int,
                                   transpose: Boolean,
                                   matrices: FloatArray) {
        activate(gl)
        glUniformMatrix4fv(uniformLocation(uniform), transpose, matrices)
    }

    private fun store(gl: GL) {
        assert { !isStored }
        isStored = true
        gl.check()
        val processor = information.preCompile(gl)
        try {
            val program = GLUtils.createProgram(shader, processor.properties())
            this.program = program.first
            uniformLocations = program.second
        } catch (e: IOException) {
            logger.error(e) { "Failed to generate shader" }
        }

        information.postCompile(gl, this)
        valid = true
        detach = gl.shaderTracker.attach(this)
    }

    companion object : KLogging()
}
