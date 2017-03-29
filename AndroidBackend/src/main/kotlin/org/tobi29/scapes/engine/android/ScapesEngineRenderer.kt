package org.tobi29.scapes.engine.android

import android.opengl.GLSurfaceView
import org.tobi29.scapes.engine.utils.Sync
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class ScapesEngineRenderer : GLSurfaceView.Renderer {
    abstract val container: AndroidContainer?
    private val sync = Sync(60.0, 5000000000L, false, "Rendering")
    private var widthResolution = 0
    private var heightResolution = 0

    override fun onSurfaceCreated(gl: GL10,
                                  config: EGLConfig) {
        container?.engine?.graphics?.reset()
        sync.init()
    }

    override fun onSurfaceChanged(gl: GL10,
                                  width: Int,
                                  height: Int) {
        widthResolution = width
        heightResolution = height
    }

    override fun onDrawFrame(gl: GL10) {
        container?.run {
            view?.let { view ->
                render(sync.delta(), view, widthResolution, heightResolution)
            }
        }
        sync.tick()
    }
}
