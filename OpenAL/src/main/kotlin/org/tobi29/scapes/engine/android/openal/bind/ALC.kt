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

object ALC {
    init {
        OpenAL.touch()
    }

    const val ALC_FALSE = 0
    const val ALC_TRUE = 1
    const val ALC_FREQUENCY = 0x1007
    const val ALC_REFRESH = 0x1008
    const val ALC_SYNC = 0x1009
    const val ALC_MONO_SOURCES = 0x1010
    const val ALC_STEREO_SOURCES = 0x1011
    const val ALC_NO_ERROR = 0
    const val ALC_INVALID_DEVICE = 0xA001
    const val ALC_INVALID_CONTEXT = 0xA002
    const val ALC_INVALID_ENUM = 0xA003
    const val ALC_INVALID_VALUE = 0xA004
    const val ALC_OUT_OF_MEMORY = 0xA005
    const val ALC_MAJOR_VERSION = 0x1000
    const val ALC_MINOR_VERSION = 0x1001
    const val ALC_ATTRIBUTES_SIZE = 0x1002
    const val ALC_ALL_ATTRIBUTES = 0x1003
    const val ALC_DEFAULT_DEVICE_SPECIFIER = 0x1004
    const val ALC_DEVICE_SPECIFIER = 0x1005
    const val ALC_EXTENSIONS = 0x1006
    const val ALC_EXT_CAPTURE = 1
    const val ALC_CAPTURE_DEVICE_SPECIFIER = 0x310
    const val ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER = 0x311
    const val ALC_CAPTURE_SAMPLES = 0x312
    const val ALC_ENUMERATE_ALL_EXT = 1
    const val ALC_DEFAULT_ALL_DEVICES_SPECIFIER = 0x1012
    const val ALC_ALL_DEVICES_SPECIFIER = 0x1013

    external fun alcCreateContext(
        p0: Long, p1: IntArray
    ): Long

    external fun alcMakeContextCurrent(
        p0: Long
    ): Boolean

    external fun alcProcessContext(
        p0: Long
    )

    external fun alcSuspendContext(
        p0: Long
    )

    external fun alcDestroyContext(
        p0: Long
    )

    external fun alcGetCurrentContext(
    ): Long

    external fun alcGetContextsDevice(
        p0: Long
    ): Long

    external fun alcOpenDevice(
        p0: String?
    ): Long

    external fun alcCloseDevice(
        p0: Long
    ): Boolean

    external fun alcGetError(
        p0: Long
    ): Int

    external fun alcIsExtensionPresent(
        p0: Long, p1: String
    ): Boolean

    external fun alcGetEnumValue(
        p0: Long, p1: String
    ): Int

    external fun alcGetString(
        p0: Long, p1: Int
    ): String

    external fun alcGetIntegerv(
        p0: Long, p1: Int, p2: Int, p3: IntArray
    )

    external fun alcCaptureOpenDevice(
        p0: String, p1: Int, p2: Int, p3: Int
    ): Long

    external fun alcCaptureCloseDevice(
        p0: Long
    ): Boolean

    external fun alcCaptureStart(
        p0: Long
    )

    external fun alcCaptureStop(
        p0: Long
    )

    external fun alcCaptureSamples(
        p0: Long, p1: ByteArray, p2: Int
    )
}
