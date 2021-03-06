/*
 * Copyright 2012-2018 Tobi29
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
import org.tobi29.math.vector.Vector2d
import org.tobi29.scapes.engine.android.AndroidKeyMap
import org.tobi29.scapes.engine.input.*
import org.tobi29.stdex.ConcurrentHashSet
import org.tobi29.stdex.InlineUtility
import org.tobi29.stdex.atomic.AtomicLong
import org.tobi29.stdex.maskAny
import org.tobi29.stdex.readOnly
import org.tobi29.utils.EventDispatcher
import org.tobi29.utils.steadyClock

internal class AndroidControllerDesktop : ControllerDesktop() {
    private val pressedMut = ConcurrentHashSet<ControllerKey>()
    override val pressed = pressedMut.readOnly()
    override var x = 0.0
        private set
    override var y = 0.0
        private set
    private var buttonState = 0
    private val lastActiveMut = AtomicLong(Long.MIN_VALUE)
    override val lastActive
        get() = lastActiveMut.get().coerceAtLeast(Long.MIN_VALUE + 1L) - 1L

    override val name = "Default"

    override val isModifierDown
        get() =
            isDown(ControllerKey.KEY_CONTROL_LEFT) ||
                    isDown(ControllerKey.KEY_CONTROL_RIGHT)

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

    internal fun addButtonState(
        state: Int,
        events: EventDispatcher
    ) {
        lastActiveMut.set(steadyClock.timeSteadyNanos())
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            MotionEvent.BUTTON_PRIMARY
                .checkButtonState(buttonState, state, events)
            MotionEvent.BUTTON_SECONDARY
                .checkButtonState(buttonState, state, events)
            MotionEvent.BUTTON_TERTIARY
                .checkButtonState(buttonState, state, events)
            MotionEvent.BUTTON_FORWARD
                .checkButtonState(buttonState, state, events)
            MotionEvent.BUTTON_BACK
                .checkButtonState(buttonState, state, events)
        }
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            MotionEvent.BUTTON_STYLUS_PRIMARY
                .checkButtonState(buttonState, state, events)
            MotionEvent.BUTTON_STYLUS_SECONDARY
                .checkButtonState(buttonState, state, events)
        }
        buttonState = state
    }

    internal fun addTypeEvent(
        character: Char,
        events: EventDispatcher
    ) {
        lastActiveMut.set(steadyClock.timeSteadyNanos())
        events.fire(ControllerKeyboard.TypeEvent(now(), character))
    }

    internal fun set(
        x: Double,
        y: Double
    ) {
        this.x = x
        this.y = y
    }

    internal fun addDelta(
        x: Double,
        y: Double,
        events: EventDispatcher
    ) {
        if (x != 0.0 && y != 0.0)
            lastActiveMut.set(steadyClock.timeSteadyNanos())
        events.fire(ControllerMouse.DeltaEvent(now(), Vector2d(x, y)))
    }

    internal fun addScroll(
        x: Double,
        y: Double,
        mode: Int,
        events: EventDispatcher
    ) {
        if (x != 0.0 && y != 0.0)
            lastActiveMut.set(steadyClock.timeSteadyNanos())
        val deltaVector = Vector2d(
            -x,
            -y
        ) // Scroll is inverted between engine and js
        val delta = when (mode) {
            0 -> ScrollDelta.Pixel(deltaVector)
            1 -> ScrollDelta.Line(deltaVector)
            2 -> ScrollDelta.Page(deltaVector)
            else -> throw IllegalArgumentException("Invalid mode: $mode")
        }
        keyPressesForScroll(delta) { key, action ->
            addPressEvent(key, action, events)
        }
        events.fire(ControllerMouse.ScrollEvent(now(), delta))
    }

    private fun Int.checkButtonState(
        from: Int,
        to: Int,
        events: EventDispatcher
    ) {
        if (gotPressed(from, to))
            AndroidKeyMap.button(this)?.let {
                addPressEvent(it, ControllerButtons.Action.PRESS, events)
            }
        else if (gotReleased(from, to))
            AndroidKeyMap.button(this)?.let {
                addPressEvent(it, ControllerButtons.Action.RELEASE, events)
            }
    }
}

@InlineUtility
@Suppress("NOTHING_TO_INLINE")
private inline fun Int.gotPressed(from: Int, to: Int) =
    !maskAny(from) && maskAny(to)

@InlineUtility
@Suppress("NOTHING_TO_INLINE")
private inline fun Int.gotReleased(from: Int, to: Int) =
    maskAny(from) && !maskAny(to)
