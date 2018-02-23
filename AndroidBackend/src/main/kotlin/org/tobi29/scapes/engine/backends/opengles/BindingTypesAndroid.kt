package org.tobi29.scapes.engine.backends.opengles

import org.tobi29.scapes.engine.Container

actual class GLESHandle(
    container: Container
) : GOSGLES(container) {
    override val glh: GLESHandle get() = this
}

actual typealias GLTexture = Int

actual inline val GLTexture_EMPTY: GLTexture get() = 0

actual typealias GLShader = Int

actual inline val GLShader_EMPTY: GLShader get() = 0

actual typealias GLProgram = Int

actual inline val GLProgram_EMPTY: GLProgram get() = 0

actual typealias GLFBO = Int

actual inline val GLFBO_EMPTY: GLFBO get() = 0

actual typealias GLFBOArray = IntArray

actual inline fun glFBOArray(
    size: Int,
    init: (Int) -> GLFBO
): GLFBOArray = IntArray(size) { init(it) }

actual typealias GLVAO = Int

actual inline val GLVAO_EMPTY: GLVAO get() = 0

actual typealias GLVBO = Int

actual inline val GLVBO_EMPTY: GLVBO get() = 0

actual typealias GLUniform = Int

actual inline val GLUniform_EMPTY: GLUniform get() = -1

actual typealias GLUniformArray = IntArray

actual inline fun glUniformArray(
    size: Int,
    init: (Int) -> GLUniform
): GLUniformArray = IntArray(size) { init(it) }
