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

package org.tobi29.scapes.engine.android

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import org.tobi29.scapes.engine.input.ControllerTouch
import java.util.concurrent.ConcurrentHashMap

class ScapesEngineView(
        context: Context,
        attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs) {
    val fingers: MutableMap<Int, ControllerTouch.Tracker> = ConcurrentHashMap()
    // TODO: Implement proper density support
    val density get() = 3.0

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        setEGLContextClientVersion(3)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        super.onTouchEvent(e)
        val density = density
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val index = e.actionIndex
                val tracker = ControllerTouch.Tracker()
                tracker.pos.set(e.getX(index) / density,
                        e.getY(index) / density)
                val id = e.getPointerId(index)
                fingers.put(id, tracker)
            }
            MotionEvent.ACTION_MOVE -> for ((key, tracker) in fingers) {
                val index = e.findPointerIndex(key)
                tracker.pos.set(e.getX(index) / density,
                        e.getY(index) / density)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                val id = e.getPointerId(e.actionIndex)
                fingers.remove(id)
            }
        }
        return true
    }
}
