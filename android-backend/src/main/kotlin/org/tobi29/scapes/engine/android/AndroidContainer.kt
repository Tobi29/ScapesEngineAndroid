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

import org.tobi29.scapes.engine.Container
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.ScapesEngineBackend
import org.tobi29.scapes.engine.backends.opengles.GLESHandle
import org.tobi29.scapes.engine.backends.opengles.GLESImpl
import org.tobi29.scapes.engine.gui.GuiController
import org.tobi29.stdex.Volatile
import org.tobi29.stdex.atomic.AtomicReference
import org.tobi29.utils.EventDispatcher

class AndroidContainer(
    backend: ScapesEngineBackend,
    private val stop: () -> Unit
) : Container, ScapesEngineBackend by backend {
    val engine get() = attached.get()?.first
    val view get() = attached.get()?.second
    val events get() = attached.get()?.third
    private val glh = GLESHandle(this)
    override val gos get() = glh
    override val formFactor = Container.FormFactor.PHONE
    override val containerWidth get() = view?.containerWidth ?: 1
    override val containerHeight get() = view?.containerHeight ?: 1
    internal val gl = GLESImpl(gos)
    private val attached =
        AtomicReference<Triple<ScapesEngine, ScapesEngineView, EventDispatcher>?>()
    @Volatile
    private var cursorCaptured = false

    init {
        AndroidKeyMap.touch()
    }

    fun attach(engine: ScapesEngine, view: ScapesEngineView) {
        val new = Triple(engine, view, EventDispatcher(engine.events) {})
        if (!attached.compareAndSet(null, new))
            throw IllegalStateException("A view is already attached")

        new.third.enable()
        view.attach(engine, this)
        events?.fire(CaptureCursorEvent(cursorCaptured))
    }

    fun detach(engine: ScapesEngine, view: ScapesEngineView) {
        val old = attached.getAndSet(null)
        if (old?.first !== engine || old.second !== view)
            throw IllegalStateException("No or a different view is attached")

        view.detach(engine, this)
        old.third.disable()
        engine.graphics.reset()
    }

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

    override fun isRenderCall() = view?.isRenderCall() == true

    override fun stop() = stop.invoke()
}
