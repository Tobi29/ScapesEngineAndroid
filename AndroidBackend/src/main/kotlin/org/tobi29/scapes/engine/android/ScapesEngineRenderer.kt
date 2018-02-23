package org.tobi29.scapes.engine.android

import android.opengl.GLSurfaceView
import org.tobi29.coroutines.Timer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ScapesEngineRenderer(val container: AndroidContainer) :
    GLSurfaceView.Renderer {
    private val timer = Timer()
    private var widthResolution = 0
    private var heightResolution = 0

    override fun onSurfaceCreated(
        gl: GL10,
        config: EGLConfig
    ) {
        container.resetGL()
        timer.init()
    }

    override fun onSurfaceChanged(
        gl: GL10,
        width: Int,
        height: Int
    ) {
        widthResolution = width
        heightResolution = height
    }

    override fun onDrawFrame(gl: GL10) {
        val tickDiff = timer.tick()
        val delta = Timer.toDelta(tickDiff).coerceIn(0.0001, 0.1)
        container.view?.let { view ->
            container.render(delta, view, widthResolution, heightResolution)
        }
    }
}
