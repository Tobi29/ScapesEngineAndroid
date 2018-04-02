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

package org.tobi29.scapes.engine.android

import org.tobi29.io.ByteBufferNative
import org.tobi29.io.ByteViewE
import org.tobi29.io.viewE
import org.tobi29.scapes.engine.Container
import org.tobi29.scapes.engine.ScapesEngineBackend
import org.tobi29.scapes.engine.backends.opengles.GLESHandle
import org.tobi29.scapes.engine.backends.opengles.GLESImpl
import org.tobi29.scapes.engine.gui.GuiController
import org.tobi29.stdex.atomic.AtomicBoolean
import org.tobi29.stdex.atomic.AtomicReference
import org.tobi29.utils.EventDispatcher
import org.tobi29.utils.sleep

class AndroidContainer(
    backend: ScapesEngineBackend,
    private val stop: () -> Unit
) : Container, ScapesEngineBackend by backend {
    val view get() = attachedView.get()?.first
    val events get() = attachedView.get()?.second
    private val glh = GLESHandle(this)
    override val gos get() = glh
    override val formFactor = Container.FormFactor.PHONE
    override val containerWidth get() = view?.containerWidth ?: 1
    override val containerHeight get() = view?.containerHeight ?: 1
    private val gl = GLESImpl(gos)
    private val attachedView =
        AtomicReference<Pair<ScapesEngineView, EventDispatcher>?>()
    private var renderThread = AtomicReference<Thread?>(null)
    @kotlin.jvm.Volatile
    private var cursorCaptured = false
    private val reset = AtomicBoolean(false)

    init {
        AndroidKeyMap.touch()
    }

    fun render(
        delta: Double,
        view: ScapesEngineView,
        width: Int,
        height: Int
    ) {
        val currentView = attachedView.get()?.first
                ?: throw IllegalStateException("No view attached")
        if (view != currentView) {
            throw IllegalStateException("Different view attached")
        }
        val currentThread = Thread.currentThread()
        if (!renderThread.compareAndSet(null, currentThread)) {
            throw IllegalStateException("Rendering twice at the same time")
        }
        if (reset.getAndSet(false)) view.engine.graphics.reset()
        try {
            while (!view.engine.graphics.render(
                    gl, delta, width, height,
                    containerWidth, containerHeight
                )) {
                sleep(1L)
            }
        } finally {
            if (!renderThread.compareAndSet(currentThread, null)) {
                throw IllegalStateException("Rendering twice at the same time")
            }
        }
    }

    fun attachView(view: ScapesEngineView) {
        val new = view to EventDispatcher(view.engine.events) {}
        if (!attachedView.compareAndSet(null, new))
            throw IllegalStateException("A view is already attached")
        view.setRenderer(ScapesEngineRenderer(this))
        new.second.enable()
        events?.fire(CaptureCursorEvent(cursorCaptured))
    }

    fun detachView(view: ScapesEngineView) {
        val old = attachedView.getAndSet(null)
        if (old?.first != view)
            throw IllegalStateException("No or a different view is attached")
        old.second.disable()
        view.engine.graphics.reset()
    }

    fun resetGL() {
        reset.set(true)
    }

    override fun allocateNative(size: Int): ByteViewE =
        ByteBufferNative(size).viewE

    override fun cursorCapture(value: Boolean) {
        cursorCaptured = value
        events?.fire(CaptureCursorEvent(value))
    }

    override fun message(
        messageType: Container.MessageType,
        title: String,
        message: String
    ) {
        events?.fire(MessageEvent(messageType, title, message))
    }

    override fun dialog(
        title: String,
        text: GuiController.TextFieldData,
        multiline: Boolean
    ) {
        events?.fire(DialogEvent(title, text, multiline))
    }

    override fun isRenderCall() = Thread.currentThread() === renderThread

    override fun stop() = stop.invoke()
}
