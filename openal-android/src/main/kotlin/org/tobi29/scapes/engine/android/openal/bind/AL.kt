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

package org.tobi29.scapes.engine.android.openal.bind

object AL {
    init {
        OpenAL.touch()
    }

    const val AL_NONE = 0
    const val AL_FALSE = 0
    const val AL_TRUE = 1
    const val AL_SOURCE_RELATIVE = 0x202
    const val AL_CONE_INNER_ANGLE = 0x1001
    const val AL_CONE_OUTER_ANGLE = 0x1002
    const val AL_PITCH = 0x1003
    const val AL_POSITION = 0x1004
    const val AL_DIRECTION = 0x1005
    const val AL_VELOCITY = 0x1006
    const val AL_LOOPING = 0x1007
    const val AL_BUFFER = 0x1009
    const val AL_GAIN = 0x100A
    const val AL_MIN_GAIN = 0x100D
    const val AL_MAX_GAIN = 0x100E
    const val AL_ORIENTATION = 0x100F
    const val AL_SOURCE_STATE = 0x1010
    const val AL_INITIAL = 0x1011
    const val AL_PLAYING = 0x1012
    const val AL_PAUSED = 0x1013
    const val AL_STOPPED = 0x1014
    const val AL_BUFFERS_QUEUED = 0x1015
    const val AL_BUFFERS_PROCESSED = 0x1016
    const val AL_REFERENCE_DISTANCE = 0x1020
    const val AL_ROLLOFF_FACTOR = 0x1021
    const val AL_CONE_OUTER_GAIN = 0x1022
    const val AL_MAX_DISTANCE = 0x1023
    const val AL_SEC_OFFSET = 0x1024
    const val AL_SAMPLE_OFFSET = 0x1025
    const val AL_BYTE_OFFSET = 0x1026
    const val AL_SOURCE_TYPE = 0x1027
    const val AL_STATIC = 0x1028
    const val AL_STREAMING = 0x1029
    const val AL_UNDETERMINED = 0x1030
    const val AL_FORMAT_MONO8 = 0x1100
    const val AL_FORMAT_MONO16 = 0x1101
    const val AL_FORMAT_STEREO8 = 0x1102
    const val AL_FORMAT_STEREO16 = 0x1103
    const val AL_FREQUENCY = 0x2001
    const val AL_BITS = 0x2002
    const val AL_CHANNELS = 0x2003
    const val AL_SIZE = 0x2004
    const val AL_UNUSED = 0x2010
    const val AL_PENDING = 0x2011
    const val AL_PROCESSED = 0x2012
    const val AL_NO_ERROR = 0
    const val AL_INVALID_NAME = 0xA001
    const val AL_INVALID_ENUM = 0xA002
    const val AL_INVALID_VALUE = 0xA003
    const val AL_INVALID_OPERATION = 0xA004
    const val AL_OUT_OF_MEMORY = 0xA005
    const val AL_VENDOR = 0xB001
    const val AL_VERSION = 0xB002
    const val AL_RENDERER = 0xB003
    const val AL_EXTENSIONS = 0xB004
    const val AL_DOPPLER_FACTOR = 0xC000
    const val AL_DOPPLER_VELOCITY = 0xC001
    const val AL_SPEED_OF_SOUND = 0xC003
    const val AL_DISTANCE_MODEL = 0xD000
    const val AL_INVERSE_DISTANCE = 0xD001
    const val AL_INVERSE_DISTANCE_CLAMPED = 0xD002
    const val AL_LINEAR_DISTANCE = 0xD003
    const val AL_LINEAR_DISTANCE_CLAMPED = 0xD004
    const val AL_EXPONENT_DISTANCE = 0xD005
    const val AL_EXPONENT_DISTANCE_CLAMPED = 0xD006

    external fun alGetError(
    ): Int

    external fun alEnable(
        p0: Int
    )

    external fun alDisable(
        p0: Int
    )

    external fun alIsEnabled(
        p0: Int
    ): Boolean

    external fun alGetBoolean(
        p0: Int
    ): Boolean

    external fun alGetInteger(
        p0: Int
    ): Int

    external fun alGetFloat(
        p0: Int
    ): Float

    external fun alGetDouble(
        p0: Int
    ): Double

    external fun alGetBooleanv(
        p0: Int, p1: BooleanArray
    )

    external fun alGetIntegerv(
        p0: Int, p1: IntArray
    )

    external fun alGetFloatv(
        p0: Int, p1: FloatArray
    )

    external fun alGetDoublev(
        p0: Int, p1: DoubleArray
    )

    external fun alGetString(
        p0: Int
    ): String

    external fun alDopplerFactor(
        p0: Float
    )

    external fun alDopplerVelocity(
        p0: Float
    )

    external fun alSpeedOfSound(
        p0: Float
    )

    external fun alDistanceModel(
        p0: Int
    )

    external fun alListenerf(
        p0: Int, p1: Float
    )

    external fun alListener3f(
        p0: Int, p1: Float, p2: Float, p3: Float
    )

    external fun alListenerfv(
        p0: Int, p1: FloatArray
    )

    external fun alListeneri(
        p0: Int, p1: Int
    )

    external fun alListener3i(
        p0: Int, p1: Int, p2: Int, p3: Int
    )

    external fun alListeneriv(
        p0: Int, p1: IntArray
    )

    external fun alGetListenerf(
        p0: Int, p1: FloatArray
    )

    external fun alGetListener3f(
        p0: Int, p1: FloatArray, p2: FloatArray, p3: FloatArray
    )

    external fun alGetListenerfv(
        p0: Int, p1: FloatArray
    )

    external fun alGetListeneri(
        p0: Int, p1: IntArray
    )

    external fun alGetListener3i(
        p0: Int, p1: IntArray, p2: IntArray, p3: IntArray
    )

    external fun alGetListeneriv(
        p0: Int, p1: IntArray
    )

    external fun alGenSources(
        p0: Int, p1: IntArray
    )

    external fun alDeleteSources(
        p0: Int, p1: IntArray
    )

    external fun alIsSource(
        p0: Int
    ): Boolean

    external fun alSourcef(
        p0: Int, p1: Int, p2: Float
    )

    external fun alSource3f(
        p0: Int, p1: Int, p2: Float, p3: Float, p4: Float
    )

    external fun alSourcefv(
        p0: Int, p1: Int, p2: FloatArray
    )

    external fun alSourcei(
        p0: Int, p1: Int, p2: Int
    )

    external fun alSource3i(
        p0: Int, p1: Int, p2: Int, p3: Int, p4: Int
    )

    external fun alSourceiv(
        p0: Int, p1: Int, p2: IntArray
    )

    external fun alGetSourcef(
        p0: Int, p1: Int, p2: FloatArray
    )

    external fun alGetSource3f(
        p0: Int, p1: Int, p2: FloatArray, p3: FloatArray, p4: FloatArray
    )

    external fun alGetSourcefv(
        p0: Int, p1: Int, p2: FloatArray
    )

    external fun alGetSourcei(
        p0: Int, p1: Int, p2: IntArray
    )

    external fun alGetSource3i(
        p0: Int, p1: Int, p2: IntArray, p3: IntArray, p4: IntArray
    )

    external fun alGetSourceiv(
        p0: Int, p1: Int, p2: IntArray
    )

    external fun alSourceQueueBuffers(
        p0: Int, p1: Int, p2: IntArray
    )

    external fun alSourceUnqueueBuffers(
        p0: Int, p1: Int, p2: IntArray
    )

    external fun alSourcePlay(
        p0: Int
    )

    external fun alSourcePause(
        p0: Int
    )

    external fun alSourceStop(
        p0: Int
    )

    external fun alSourceRewind(
        p0: Int
    )

    external fun alSourcePlayv(
        p0: Int, p1: IntArray
    )

    external fun alSourcePausev(
        p0: Int, p1: IntArray
    )

    external fun alSourceStopv(
        p0: Int, p1: IntArray
    )

    external fun alSourceRewindv(
        p0: Int, p1: IntArray
    )

    external fun alGenBuffers(
        p0: Int, p1: IntArray
    )

    external fun alDeleteBuffers(
        p0: Int, p1: IntArray
    )

    external fun alIsBuffer(
        p0: Int
    ): Boolean

    external fun alBufferf(
        p0: Int, p1: Int, p2: Float
    )

    external fun alBuffer3f(
        p0: Int, p1: Int, p2: Float, p3: Float, p4: Float
    )

    external fun alBufferfv(
        p0: Int, p1: Int, p2: FloatArray
    )

    external fun alBufferi(
        p0: Int, p1: Int, p2: Int
    )

    external fun alBuffer3i(
        p0: Int, p1: Int, p2: Int, p3: Int, p4: Int
    )

    external fun alBufferiv(
        p0: Int, p1: Int, p2: IntArray
    )

    external fun alGetBufferf(
        p0: Int, p1: Int, p2: FloatArray
    )

    external fun alGetBuffer3f(
        p0: Int, p1: Int, p2: FloatArray, p3: FloatArray, p4: FloatArray
    )

    external fun alGetBufferfv(
        p0: Int, p1: Int, p2: FloatArray
    )

    external fun alGetBufferi(
        p0: Int, p1: Int, p2: IntArray
    )

    external fun alGetBuffer3i(
        p0: Int, p1: Int, p2: IntArray, p3: IntArray, p4: IntArray
    )

    external fun alGetBufferiv(
        p0: Int, p1: Int, p2: IntArray
    )

    external fun alBufferData(
        p0: Int, p1: Int, p2: ByteArray, p3: Int, p4: Int
    )
}
