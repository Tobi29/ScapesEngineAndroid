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

package org.tobi29.scapes.engine.android.openal

import android.content.Context
import android.media.AudioManager
import org.tobi29.io.ByteViewRO
import org.tobi29.io.readAsByteArray
import org.tobi29.logging.KLogging
import org.tobi29.math.vector.Vector3d
import org.tobi29.scapes.engine.android.openal.bind.AL
import org.tobi29.scapes.engine.android.openal.bind.ALC
import org.tobi29.scapes.engine.android.openal.bind.SOFTPauseDevice
import org.tobi29.scapes.engine.backends.openal.openal.OpenAL
import org.tobi29.scapes.engine.sound.AudioFormat
import org.tobi29.scapes.engine.sound.SoundException
import org.tobi29.stdex.math.toRad
import kotlin.math.cos
import kotlin.math.sin

class AndroidOpenAL(private val androidContext: Context) : OpenAL {
    private var device = 0L
    private var context = 0L
    private val floatBuffer = FloatArray(6)
    private val intBuffer = IntArray(1)

    override fun checkError(message: String) {
        val error = AL.alGetError()
        if (error != AL.AL_NO_ERROR) {
            throw SoundException(
                AL.alGetString(error) + " in " + message
            )
        }
    }

    override fun create(speedOfSound: Double) {
        val audioManager = androidContext.getSystemService(
            Context.AUDIO_SERVICE
        ) as AudioManager
        val contextAttributes = IntArray(3)
        contextAttributes[0] = ALC.ALC_FREQUENCY
        contextAttributes[1] = audioManager.getProperty(
            AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE
        ).toInt()

        device = ALC.alcOpenDevice(null)
        if (device == 0L) {
            throw IllegalStateException(
                "Failed to open the default device."
            )
        }
        context = ALC.alcCreateContext(device, contextAttributes)
        if (context == 0L) {
            throw IllegalStateException(
                "Failed to create an OpenAL context."
            )
        }
        ALC.alcMakeContextCurrent(context)
        logger.info {
            "OpenAL: ${AL.alGetString(
                AL.AL_VERSION
            )} (Vendor: ${AL.alGetString(
                AL.AL_VENDOR
            )}, Renderer: ${AL.alGetString(
                AL.AL_RENDERER
            )})"
        }
        AL.alSpeedOfSound(speedOfSound.toFloat())
        AL.alDistanceModel(AL.AL_INVERSE_DISTANCE_CLAMPED)
        floatBuffer[0] = 0.0f
        floatBuffer[1] = -1.0f
        floatBuffer[2] = 0.0f
        floatBuffer[3] = 0.0f
        floatBuffer[4] = 0.0f
        floatBuffer[5] = 1.0f
        AL.alListenerfv(AL.AL_ORIENTATION, floatBuffer)
        AL.alListener3f(AL.AL_POSITION, 0.0f, 0.0f, 0.0f)
        AL.alListener3f(AL.AL_VELOCITY, 0.0f, 0.0f, 0.0f)
    }

    override fun resume() {
        if (device != 0L) SOFTPauseDevice.alcDeviceResumeSOFT(device)
    }

    override fun pause() {
        if (device != 0L) SOFTPauseDevice.alcDevicePauseSOFT(device)
    }

    override fun destroy() {
        if (context != 0L) {
            ALC.alcDestroyContext(context)
            context = 0
        }
        if (device != 0L) {
            ALC.alcCloseDevice(device)
            device = 0
        }
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
        floatBuffer[0] = lookX
        floatBuffer[1] = lookY
        floatBuffer[2] = lookZ
        floatBuffer[3] = 0.0f
        floatBuffer[4] = 0.0f
        floatBuffer[5] = 1.0f
        AL.alListenerfv(AL.AL_ORIENTATION, floatBuffer)
        AL.alListener3f(
            AL.AL_POSITION, position.x.toFloat(),
            position.y.toFloat(), position.z.toFloat()
        )
        AL.alListener3f(
            AL.AL_VELOCITY, velocity.x.toFloat(),
            velocity.y.toFloat(), velocity.z.toFloat()
        )
    }

    override fun createSource(): Int {
        AL.alGenSources(1, intBuffer)
        return intBuffer[0]
    }

    override fun deleteSource(id: Int) {
        intBuffer[0] = id
        AL.alDeleteSources(1, intBuffer)
    }

    override fun setBuffer(
        id: Int,
        value: Int
    ) {
        AL.alSourcei(id, AL.AL_BUFFER, value)
    }

    override fun setPitch(
        id: Int,
        value: Double
    ) {
        AL.alSourcef(id, AL.AL_PITCH, value.toFloat())
    }

    override fun setGain(
        id: Int,
        value: Double
    ) {
        AL.alSourcef(id, AL.AL_GAIN, value.toFloat())
    }

    override fun setLooping(
        id: Int,
        value: Boolean
    ) {
        AL.alSourcei(
            id, AL.AL_LOOPING,
            if (value) AL.AL_TRUE else AL.AL_FALSE
        )
    }

    override fun setRelative(
        id: Int,
        value: Boolean
    ) {
        AL.alSourcei(
            id, AL.AL_SOURCE_RELATIVE,
            if (value) AL.AL_TRUE else AL.AL_FALSE
        )
    }

    override fun setPosition(
        id: Int,
        pos: Vector3d
    ) {
        AL.alSource3f(
            id, AL.AL_POSITION, pos.x.toFloat(), pos.y.toFloat(),
            pos.z.toFloat()
        )
    }

    override fun setVelocity(
        id: Int,
        vel: Vector3d
    ) {
        AL.alSource3f(
            id, AL.AL_VELOCITY, vel.x.toFloat(), vel.y.toFloat(),
            vel.z.toFloat()
        )
    }

    override fun setReferenceDistance(
        id: Int,
        value: Double
    ) {
        AL.alSourcef(id, AL.AL_REFERENCE_DISTANCE, value.toFloat())
    }

    override fun setRolloffFactor(
        id: Int,
        value: Double
    ) {
        AL.alSourcef(id, AL.AL_ROLLOFF_FACTOR, value.toFloat())
    }

    override fun setMaxDistance(
        id: Int,
        value: Double
    ) {
        AL.alSourcef(id, AL.AL_MAX_DISTANCE, value.toFloat())
    }

    override fun play(id: Int) {
        AL.alSourcePlay(id)
    }

    override fun stop(id: Int) {
        AL.alSourceStop(id)
    }

    override fun createBuffer(): Int {
        AL.alGenBuffers(1, intBuffer)
        return intBuffer[0]
    }

    override fun deleteBuffer(id: Int) {
        intBuffer[0] = id
        AL.alDeleteBuffers(1, intBuffer)
    }

    override fun storeBuffer(
        id: Int,
        format: AudioFormat,
        buffer: ByteViewRO,
        rate: Int
    ) {
        AL.alBufferData(
            id,
            when (format) {
                AudioFormat.MONO -> AL.AL_FORMAT_MONO16
                AudioFormat.STEREO -> AL.AL_FORMAT_STEREO16
            },
            buffer.readAsByteArray(), buffer.size,
            rate
        )
    }

    override fun isPlaying(id: Int): Boolean {
        AL.alGetSourcei(id, AL.AL_SOURCE_STATE, intBuffer)
        return intBuffer[0] == AL.AL_PLAYING
    }

    override fun isStopped(id: Int): Boolean {
        AL.alGetSourcei(id, AL.AL_SOURCE_STATE, intBuffer)
        return intBuffer[0] != AL.AL_PLAYING && intBuffer[0] != AL.AL_PAUSED
    }

    override fun getBuffersQueued(id: Int): Int {
        AL.alGetSourcei(id, AL.AL_BUFFERS_QUEUED, intBuffer)
        return intBuffer[0]
    }

    override fun getBuffersProcessed(id: Int): Int {
        AL.alGetSourcei(id, AL.AL_BUFFERS_PROCESSED, intBuffer)
        return intBuffer[0]
    }

    override fun queue(
        id: Int,
        buffer: Int
    ) {
        intBuffer[0] = buffer
        AL.alSourceQueueBuffers(id, 1, intBuffer)
    }

    override fun unqueue(id: Int): Int {
        AL.alSourceUnqueueBuffers(id, 1, intBuffer)
        return intBuffer[0]
    }

    override fun getBuffer(id: Int): Int {
        AL.alGetSourcei(id, AL.AL_BUFFER, intBuffer)
        return intBuffer[0]
    }

    companion object : KLogging()
}
