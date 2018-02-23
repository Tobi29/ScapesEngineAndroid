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

import org.tobi29.arrays.sliceOver
import org.tobi29.scapes.engine.input.ControllerButtons
import org.tobi29.scapes.engine.input.ControllerJoystick
import org.tobi29.scapes.engine.input.ControllerKey
import org.tobi29.scapes.engine.input.now
import org.tobi29.stdex.ConcurrentHashSet
import org.tobi29.stdex.atomic.AtomicLong
import org.tobi29.stdex.readOnly
import org.tobi29.utils.EventDispatcher
import org.tobi29.utils.steadyClock

internal class AndroidControllerJoystick(
    override val name: String,
    override val type: ControllerJoystick.Type,
    axisCount: Int
) : ControllerJoystick() {
    private val pressedMut = ConcurrentHashSet<ControllerKey>()
    override val pressed = pressedMut.readOnly()
    override val axes = DoubleArray(axisCount).sliceOver()
    private val lastActiveMut = AtomicLong(Long.MIN_VALUE)
    override val lastActive get() = lastActiveMut.get()

    override fun isDown(key: ControllerKey) = key in pressedMut

    internal fun addPressEvent(
        key: ControllerKey,
        action: ControllerButtons.Action,
        events: EventDispatcher
    ) {
        lastActiveMut.set(steadyClock.timeSteadyNanos())
        synchronized(this) {
            when (action) {
                ControllerButtons.Action.PRESS -> pressedMut.add(key)
                ControllerButtons.Action.RELEASE -> pressedMut.remove(key)
            }
            events.fire(ControllerButtons.PressEvent(now(), key, action))
        }
    }

    internal fun setAxis(
        axis: Int,
        value: Double,
        events: EventDispatcher
    ) {
        synchronized(this) {
            if (axes[axis] < 0.5 && value >= 0.5) {
                lastActiveMut.set(steadyClock.timeSteadyNanos())
                ControllerKey.axis(axis)?.let {
                    addPressEvent(
                        it, ControllerButtons.Action.PRESS,
                        events
                    )
                }
            } else if (axes[axis] >= 0.5 && value < 0.5) {
                lastActiveMut.set(steadyClock.timeSteadyNanos())
                ControllerKey.axis(axis)?.let {
                    addPressEvent(
                        it, ControllerButtons.Action.RELEASE,
                        events
                    )
                }
            }
            if (axes[axis] > -0.5 && value <= -0.5) {
                lastActiveMut.set(steadyClock.timeSteadyNanos())
                ControllerKey.axisNegative(axis)?.let {
                    addPressEvent(
                        it, ControllerButtons.Action.PRESS,
                        events
                    )
                }
            } else if (axes[axis] <= -0.5 && value > -0.5) {
                lastActiveMut.set(steadyClock.timeSteadyNanos())
                ControllerKey.axisNegative(axis)?.let {
                    addPressEvent(
                        it, ControllerButtons.Action.RELEASE,
                        events
                    )
                }
            }
            axes[axis] = value
        }
    }
}
