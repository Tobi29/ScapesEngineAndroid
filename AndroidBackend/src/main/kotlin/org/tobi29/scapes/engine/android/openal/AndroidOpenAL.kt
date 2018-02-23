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

package org.tobi29.scapes.engine.android.openal

import android.content.Context
import android.media.AudioManager
import org.tobi29.io.ByteViewRO
import org.tobi29.io.readAsByteArray
import org.tobi29.logging.KLogging
import org.tobi29.math.vector.Vector3d
import org.tobi29.scapes.engine.backends.openal.openal.OpenAL
import org.tobi29.scapes.engine.sound.AudioFormat
import org.tobi29.scapes.engine.sound.SoundException
import org.tobi29.stdex.math.toRad
import paulscode.android.sound.ALAN
import kotlin.math.cos
import kotlin.math.sin

class AndroidOpenAL(private val context: Context) : OpenAL {
    private val intBuffer = IntArray(1)

    override fun checkError(message: String) {
        val error = ALAN.alGetError()
        if (error != ALAN.AL_NO_ERROR) {
            throw SoundException(
                ALAN.alGetString(error) + " in " + message
            )
        }
    }

    override fun create(speedOfSound: Double) {
        val audioManager = context.getSystemService(
            Context.AUDIO_SERVICE
        ) as AudioManager
        val contextAttributes = IntArray(3)
        contextAttributes[0] = ALAN.ALC_FREQUENCY
        contextAttributes[1] = audioManager.getProperty(
            AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE
        ).toInt()
        ALAN.create(contextAttributes)
        logger.info(
            "OpenAL: ${ALAN.alGetString(
                ALAN.AL_VERSION
            )} (Vendor: ${ALAN.alGetString(
                ALAN.AL_VENDOR
            )}, Renderer: ${ALAN.alGetString(
                ALAN.AL_RENDERER
            )})"
        )
        ALAN.alSpeedOfSound(speedOfSound.toFloat())
        ALAN.alListenerfv(
            ALAN.AL_ORIENTATION,
            floatArrayOf(0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f)
        )
        ALAN.alListener3f(ALAN.AL_POSITION, 0.0f, 0.0f, 0.0f)
        ALAN.alListener3f(ALAN.AL_VELOCITY, 0.0f, 0.0f, 0.0f)
    }

    override fun destroy() {
        ALAN.destroy()
    }

    override fun setListener(
        position: Vector3d,
        orientation: Vector3d,
        velocity: Vector3d
    ) {
        val cos = cos(orientation.x.toFloat().toRad())
        val lookX = cos(orientation.z.toFloat().toRad()) * cos
        val lookY = sin(orientation.z.toFloat().toRad()) * cos
        val lookZ = sin(orientation.x.toFloat().toRad())
        ALAN.alListenerfv(
            ALAN.AL_ORIENTATION,
            floatArrayOf(lookX, lookY, lookZ, 0.0f, 0.0f, 1.0f)
        )
        ALAN.alListener3f(
            ALAN.AL_POSITION, position.x.toFloat(),
            position.y.toFloat(), position.z.toFloat()
        )
        ALAN.alListener3f(
            ALAN.AL_VELOCITY, velocity.x.toFloat(),
            velocity.y.toFloat(), velocity.z.toFloat()
        )
    }

    override fun createSource(): Int {
        ALAN.alGenSources(1, intBuffer)
        return intBuffer[0]
    }

    override fun deleteSource(id: Int) {
        intBuffer[0] = id
        ALAN.alDeleteSources(1, intBuffer)
    }

    override fun setBuffer(
        id: Int,
        value: Int
    ) {
        ALAN.alSourcei(id, ALAN.AL_BUFFER, value)
    }

    override fun setPitch(
        id: Int,
        value: Double
    ) {
        ALAN.alSourcef(id, ALAN.AL_PITCH, value.toFloat())
    }

    override fun setGain(
        id: Int,
        value: Double
    ) {
        ALAN.alSourcef(id, ALAN.AL_GAIN, value.toFloat())
    }

    override fun setReferenceDistance(
        id: Int,
        value: Double
    ) {
        ALAN.alSourcef(id, ALAN.AL_REFERENCE_DISTANCE, value.toFloat())
    }

    override fun setRolloffFactor(
        id: Int,
        value: Double
    ) {
        ALAN.alSourcef(id, ALAN.AL_ROLLOFF_FACTOR, value.toFloat())
    }

    override fun setMaxDistance(
        id: Int,
        value: Double
    ) {
        ALAN.alSourcef(id, ALAN.AL_MAX_DISTANCE, value.toFloat())
    }

    override fun setLooping(
        id: Int,
        value: Boolean
    ) {
        ALAN.alSourcei(
            id, ALAN.AL_LOOPING,
            if (value) ALAN.AL_TRUE else ALAN.AL_FALSE
        )
    }

    override fun setRelative(
        id: Int,
        value: Boolean
    ) {
        ALAN.alSourcei(
            id, ALAN.AL_SOURCE_RELATIVE,
            if (value) ALAN.AL_TRUE else ALAN.AL_FALSE
        )
    }

    override fun setPosition(
        id: Int,
        pos: Vector3d
    ) {
        ALAN.alSource3f(
            id, ALAN.AL_POSITION, pos.x.toFloat(), pos.y.toFloat(),
            pos.z.toFloat()
        )
    }

    override fun setVelocity(
        id: Int,
        vel: Vector3d
    ) {
        ALAN.alSource3f(
            id, ALAN.AL_VELOCITY, vel.x.toFloat(), vel.y.toFloat(),
            vel.z.toFloat()
        )
    }

    override fun play(id: Int) {
        ALAN.alSourcePlay(id)
    }

    override fun stop(id: Int) {
        ALAN.alSourceStop(id)
    }

    override fun createBuffer(): Int {
        ALAN.alGenBuffers(1, intBuffer)
        return intBuffer[0]
    }

    override fun deleteBuffer(id: Int) {
        intBuffer[0] = id
        ALAN.alDeleteBuffers(1, intBuffer)
    }

    override fun storeBuffer(
        id: Int,
        format: AudioFormat,
        buffer: ByteViewRO,
        rate: Int
    ) {
        when (format) {
            AudioFormat.MONO -> ALAN.alBufferData(
                id, ALAN.AL_FORMAT_MONO16,
                buffer.readAsByteArray(),
                buffer.size, rate
            )
            AudioFormat.STEREO -> ALAN.alBufferData(
                id, ALAN.AL_FORMAT_STEREO16,
                buffer.readAsByteArray(),
                buffer.size, rate
            )
        }
    }

    override fun isPlaying(id: Int): Boolean {
        ALAN.alGetSourcei(id, ALAN.AL_SOURCE_STATE, intBuffer)
        return intBuffer[0] == ALAN.AL_PLAYING
    }

    override fun isStopped(id: Int): Boolean {
        ALAN.alGetSourcei(id, ALAN.AL_SOURCE_STATE, intBuffer)
        val state = intBuffer[0]
        return state != ALAN.AL_PLAYING && state != ALAN.AL_PAUSED
    }

    override fun getBuffersQueued(id: Int): Int {
        ALAN.alGetSourcei(id, ALAN.AL_BUFFERS_QUEUED, intBuffer)
        return intBuffer[0]
    }

    override fun getBuffersProcessed(id: Int): Int {
        ALAN.alGetSourcei(id, ALAN.AL_BUFFERS_PROCESSED, intBuffer)
        return intBuffer[0]
    }

    override fun queue(
        id: Int,
        buffer: Int
    ) {
        intBuffer[0] = buffer
        ALAN.alSourceQueueBuffers(id, 1, intBuffer)
    }

    override fun unqueue(id: Int): Int {
        ALAN.alSourceUnqueueBuffers(id, 1, intBuffer)
        return intBuffer[0]
    }

    override fun getBuffer(id: Int): Int {
        ALAN.alGetSourcei(id, ALAN.AL_BUFFER, intBuffer)
        return intBuffer[0]
    }

    companion object : KLogging()
}
