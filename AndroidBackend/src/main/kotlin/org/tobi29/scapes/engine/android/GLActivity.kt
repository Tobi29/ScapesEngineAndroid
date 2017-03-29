package org.tobi29.scapes.engine.android

import android.app.Activity
import android.os.Bundle

open class GLActivity : Activity() {
    var view: ScapesEngineView? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ScapesEngineView(this)
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        view?.onResume()
    }

    override fun onPause() {
        super.onPause()
        view?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        view = null
    }
}
