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

package org.tobi29.scapes.engine.android.input

import android.view.MotionEvent
import org.tobi29.scapes.engine.input.ControllerTouch
import org.tobi29.scapes.engine.input.ControllerTracker
import org.tobi29.stdex.ConcurrentHashMap
import org.tobi29.stdex.atomic.AtomicLong
import org.tobi29.utils.steadyClock

internal class AndroidControllerTouch : ControllerTouch() {
    override val name = "Touchscreen"
    private val lastActiveMut = AtomicLong(Long.MIN_VALUE)
    override val lastActive get() = lastActiveMut.get()
    private val fingersMut = ConcurrentHashMap<Int, ControllerTracker.Tracker>()

    override fun fingers() = fingersMut.values.asSequence()

    internal fun handle(
        event: MotionEvent,
        densityX: Double,
        densityY: Double
    ) {
        lastActiveMut.set(steadyClock.timeSteadyNanos())
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                val tracker = ControllerTracker.Tracker()
                tracker.pos.setXY(
                    event.getX(index) / densityX,
                    event.getY(index) / densityY
                )
                val id = event.getPointerId(index)
                fingersMut.put(id, tracker)
            }
            MotionEvent.ACTION_MOVE -> for ((key, tracker) in fingersMut) {
                val index = event.findPointerIndex(key)
                tracker.pos.setXY(
                    event.getX(index) / densityX,
                    event.getY(index) / densityY
                )
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                val id = event.getPointerId(event.actionIndex)
                fingersMut.remove(id)
            }
        }
    }
}
