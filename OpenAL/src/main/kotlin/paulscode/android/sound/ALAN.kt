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

package paulscode.android.sound

object ALAN {
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
    const val AL_REFERENCE_DISTANCE = 0x1020
    const val AL_ROLLOFF_FACTOR = 0x1021
    const val AL_CONE_OUTER_GAIN = 0x1022
    const val AL_MAX_DISTANCE = 0x1023
    const val AL_FREQUENCY = 0x2001
    const val AL_BITS = 0x2002
    const val AL_CHANNELS = 0x2003
    const val AL_SIZE = 0x2004
    const val AL_UNUSED = 0x2010
    const val AL_PENDING = 0x2011
    const val AL_PROCESSED = 0x2012
    const val AL_NO_ERROR = AL_FALSE
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
    const val ALC_FALSE = 0
    const val ALC_TRUE = 1
    const val ALC_FREQUENCY = 0x1007
    const val ALC_REFRESH = 0x1008
    const val ALC_SYNC = 0x1009
    const val ALC_MONO_SOURCES = 0x1010
    const val ALC_STEREO_SOURCES = 0x1011
    const val ALC_NO_ERROR = ALC_FALSE
    const val ALC_INVALID_DEVICE = 0xA001
    const val ALC_INVALID_CONTEXT = 0xA002
    const val ALC_INVALID_ENUM = 0xA003
    const val ALC_INVALID_VALUE = 0xA004
    const val ALC_OUT_OF_MEMORY = 0xA005
    const val ALC_DEFAULT_DEVICE_SPECIFIER = 0x1004
    const val ALC_DEVICE_SPECIFIER = 0x1005
    const val ALC_EXTENSIONS = 0x1006
    const val ALC_MAJOR_VERSION = 0x1000
    const val ALC_MINOR_VERSION = 0x1001
    const val ALC_ATTRIBUTES_SIZE = 0x1002
    const val ALC_ALL_ATTRIBUTES = 0x1003
    const val ALC_CAPTURE_DEVICE_SPECIFIER = 0x310
    const val ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER = 0x311
    const val ALC_CAPTURE_SAMPLES = 0x312
    const val AL_FORMAT_IMA_ADPCM_MONO16_EXT = 0x10000
    const val AL_FORMAT_IMA_ADPCM_STEREO16_EXT = 0x10001
    const val AL_FORMAT_WAVE_EXT = 0x10002
    const val AL_FORMAT_VORBIS_EXT = 0x10003
    const val AL_FORMAT_QUAD8_LOKI = 0x10004
    const val AL_FORMAT_QUAD16_LOKI = 0x10005
    const val AL_FORMAT_MONO_FLOAT32 = 0x10010
    const val AL_FORMAT_STEREO_FLOAT32 = 0x10011
    const val AL_FORMAT_MONO_DOUBLE_EXT = 0x10012
    const val AL_FORMAT_STEREO_DOUBLE_EXT = 0x10013
    const val ALC_CHAN_MAIN_LOKI = 0x500001
    const val ALC_CHAN_PCM_LOKI = 0x500002
    const val ALC_CHAN_CD_LOKI = 0x500003
    const val ALC_DEFAULT_ALL_DEVICES_SPECIFIER = 0x1012
    const val ALC_ALL_DEVICES_SPECIFIER = 0x1013
    const val AL_FORMAT_QUAD8 = 0x1204
    const val AL_FORMAT_QUAD16 = 0x1205
    const val AL_FORMAT_QUAD32 = 0x1206
    const val AL_FORMAT_REAR8 = 0x1207
    const val AL_FORMAT_REAR16 = 0x1208
    const val AL_FORMAT_REAR32 = 0x1209
    const val AL_FORMAT_51CHN8 = 0x120A
    const val AL_FORMAT_51CHN16 = 0x120B
    const val AL_FORMAT_51CHN32 = 0x120C
    const val AL_FORMAT_61CHN8 = 0x120D
    const val AL_FORMAT_61CHN16 = 0x120E
    const val AL_FORMAT_61CHN32 = 0x120F
    const val AL_FORMAT_71CHN8 = 0x1210
    const val AL_FORMAT_71CHN16 = 0x1211
    const val AL_FORMAT_71CHN32 = 0x1212
    const val AL_FORMAT_MONO_MULAW = 0x10014
    const val AL_FORMAT_STEREO_MULAW = 0x10015
    const val AL_FORMAT_QUAD_MULAW = 0x10021
    const val AL_FORMAT_REAR_MULAW = 0x10022
    const val AL_FORMAT_51CHN_MULAW = 0x10023
    const val AL_FORMAT_61CHN_MULAW = 0x10024
    const val AL_FORMAT_71CHN_MULAW = 0x10025
    const val AL_FORMAT_MONO_IMA4 = 0x1300
    const val AL_FORMAT_STEREO_IMA4 = 0x1301
    const val ALC_CONNECTED = 0x313
    const val AL_SOURCE_DISTANCE_MODEL = 0x200
    const val ALC_EFX_MAJOR_VERSION = 0x20001
    const val ALC_EFX_MINOR_VERSION = 0x20002
    const val ALC_MAX_AUXILIARY_SENDS = 0x20003
    const val AL_METERS_PER_UNIT = 0x20004
    const val AL_DIRECT_FILTER = 0x20005
    const val AL_AUXILIARY_SEND_FILTER = 0x20006
    const val AL_AIR_ABSORPTION_FACTOR = 0x20007
    const val AL_ROOM_ROLLOFF_FACTOR = 0x20008
    const val AL_CONE_OUTER_GAINHF = 0x20009
    const val AL_DIRECT_FILTER_GAINHF_AUTO = 0x2000A
    const val AL_AUXILIARY_SEND_FILTER_GAIN_AUTO = 0x2000B
    const val AL_AUXILIARY_SEND_FILTER_GAINHF_AUTO = 0x2000C
    const val AL_REVERB_DENSITY = 0x0001
    const val AL_REVERB_DIFFUSION = 0x0002
    const val AL_REVERB_GAIN = 0x0003
    const val AL_REVERB_GAINHF = 0x0004
    const val AL_REVERB_DECAY_TIME = 0x0005
    const val AL_REVERB_DECAY_HFRATIO = 0x0006
    const val AL_REVERB_REFLECTIONS_GAIN = 0x0007
    const val AL_REVERB_REFLECTIONS_DELAY = 0x0008
    const val AL_REVERB_LATE_REVERB_GAIN = 0x0009
    const val AL_REVERB_LATE_REVERB_DELAY = 0x000A
    const val AL_REVERB_AIR_ABSORPTION_GAINHF = 0x000B
    const val AL_REVERB_ROOM_ROLLOFF_FACTOR = 0x000C
    const val AL_REVERB_DECAY_HFLIMIT = 0x000D
    const val AL_EAXREVERB_DENSITY = 0x0001
    const val AL_EAXREVERB_DIFFUSION = 0x0002
    const val AL_EAXREVERB_GAIN = 0x0003
    const val AL_EAXREVERB_GAINHF = 0x0004
    const val AL_EAXREVERB_GAINLF = 0x0005
    const val AL_EAXREVERB_DECAY_TIME = 0x0006
    const val AL_EAXREVERB_DECAY_HFRATIO = 0x0007
    const val AL_EAXREVERB_DECAY_LFRATIO = 0x0008
    const val AL_EAXREVERB_REFLECTIONS_GAIN = 0x0009
    const val AL_EAXREVERB_REFLECTIONS_DELAY = 0x000A
    const val AL_EAXREVERB_REFLECTIONS_PAN = 0x000B
    const val AL_EAXREVERB_LATE_REVERB_GAIN = 0x000C
    const val AL_EAXREVERB_LATE_REVERB_DELAY = 0x000D
    const val AL_EAXREVERB_LATE_REVERB_PAN = 0x000E
    const val AL_EAXREVERB_ECHO_TIME = 0x000F
    const val AL_EAXREVERB_ECHO_DEPTH = 0x0010
    const val AL_EAXREVERB_MODULATION_TIME = 0x0011
    const val AL_EAXREVERB_MODULATION_DEPTH = 0x0012
    const val AL_EAXREVERB_AIR_ABSORPTION_GAINHF = 0x0013
    const val AL_EAXREVERB_HFREFERENCE = 0x0014
    const val AL_EAXREVERB_LFREFERENCE = 0x0015
    const val AL_EAXREVERB_ROOM_ROLLOFF_FACTOR = 0x0016
    const val AL_EAXREVERB_DECAY_HFLIMIT = 0x0017
    const val AL_CHORUS_WAVEFORM = 0x0001
    const val AL_CHORUS_PHASE = 0x0002
    const val AL_CHORUS_RATE = 0x0003
    const val AL_CHORUS_DEPTH = 0x0004
    const val AL_CHORUS_FEEDBACK = 0x0005
    const val AL_CHORUS_DELAY = 0x0006
    const val AL_DISTORTION_EDGE = 0x0001
    const val AL_DISTORTION_GAIN = 0x0002
    const val AL_DISTORTION_LOWPASS_CUTOFF = 0x0003
    const val AL_DISTORTION_EQCENTER = 0x0004
    const val AL_DISTORTION_EQBANDWIDTH = 0x0005
    const val AL_ECHO_DELAY = 0x0001
    const val AL_ECHO_LRDELAY = 0x0002
    const val AL_ECHO_DAMPING = 0x0003
    const val AL_ECHO_FEEDBACK = 0x0004
    const val AL_ECHO_SPREAD = 0x0005
    const val AL_FLANGER_WAVEFORM = 0x0001
    const val AL_FLANGER_PHASE = 0x0002
    const val AL_FLANGER_RATE = 0x0003
    const val AL_FLANGER_DEPTH = 0x0004
    const val AL_FLANGER_FEEDBACK = 0x0005
    const val AL_FLANGER_DELAY = 0x0006
    const val AL_FREQUENCY_SHIFTER_FREQUENCY = 0x0001
    const val AL_FREQUENCY_SHIFTER_LEFT_DIRECTION = 0x0002
    const val AL_FREQUENCY_SHIFTER_RIGHT_DIRECTION = 0x0003
    const val AL_VOCAL_MORPHER_PHONEMEA = 0x0001
    const val AL_VOCAL_MORPHER_PHONEMEA_COARSE_TUNING = 0x0002
    const val AL_VOCAL_MORPHER_PHONEMEB = 0x0003
    const val AL_VOCAL_MORPHER_PHONEMEB_COARSE_TUNING = 0x0004
    const val AL_VOCAL_MORPHER_WAVEFORM = 0x0005
    const val AL_VOCAL_MORPHER_RATE = 0x0006
    const val AL_PITCH_SHIFTER_COARSE_TUNE = 0x0001
    const val AL_PITCH_SHIFTER_FINE_TUNE = 0x0002
    const val AL_RING_MODULATOR_FREQUENCY = 0x0001
    const val AL_RING_MODULATOR_HIGHPASS_CUTOFF = 0x0002
    const val AL_RING_MODULATOR_WAVEFORM = 0x0003
    const val AL_AUTOWAH_ATTACK_TIME = 0x0001
    const val AL_AUTOWAH_RELEASE_TIME = 0x0002
    const val AL_AUTOWAH_RESONANCE = 0x0003
    const val AL_AUTOWAH_PEAK_GAIN = 0x0004
    const val AL_COMPRESSOR_ONOFF = 0x0001
    const val AL_EQUALIZER_LOW_GAIN = 0x0001
    const val AL_EQUALIZER_LOW_CUTOFF = 0x0002
    const val AL_EQUALIZER_MID1_GAIN = 0x0003
    const val AL_EQUALIZER_MID1_CENTER = 0x0004
    const val AL_EQUALIZER_MID1_WIDTH = 0x0005
    const val AL_EQUALIZER_MID2_GAIN = 0x0006
    const val AL_EQUALIZER_MID2_CENTER = 0x0007
    const val AL_EQUALIZER_MID2_WIDTH = 0x0008
    const val AL_EQUALIZER_HIGH_GAIN = 0x0009
    const val AL_EQUALIZER_HIGH_CUTOFF = 0x000A
    const val AL_EFFECT_FIRST_PARAMETER = 0x0000
    const val AL_EFFECT_LAST_PARAMETER = 0x8000
    const val AL_EFFECT_TYPE = 0x8001
    const val AL_EFFECT_NULL = 0x0000
    const val AL_EFFECT_REVERB = 0x0001
    const val AL_EFFECT_CHORUS = 0x0002
    const val AL_EFFECT_DISTORTION = 0x0003
    const val AL_EFFECT_ECHO = 0x0004
    const val AL_EFFECT_FLANGER = 0x0005
    const val AL_EFFECT_FREQUENCY_SHIFTER = 0x0006
    const val AL_EFFECT_VOCAL_MORPHER = 0x0007
    const val AL_EFFECT_PITCH_SHIFTER = 0x0008
    const val AL_EFFECT_RING_MODULATOR = 0x0009
    const val AL_EFFECT_AUTOWAH = 0x000A
    const val AL_EFFECT_COMPRESSOR = 0x000B
    const val AL_EFFECT_EQUALIZER = 0x000C
    const val AL_EFFECT_EAXREVERB = 0x8000
    const val AL_EFFECTSLOT_EFFECT = 0x0001
    const val AL_EFFECTSLOT_GAIN = 0x0002
    const val AL_EFFECTSLOT_AUXILIARY_SEND_AUTO = 0x0003
    const val AL_EFFECTSLOT_NULL = 0x0000
    const val AL_LOWPASS_GAIN = 0x0001
    const val AL_LOWPASS_GAINHF = 0x0002
    const val AL_HIGHPASS_GAIN = 0x0001
    const val AL_HIGHPASS_GAINLF = 0x0002
    const val AL_BANDPASS_GAIN = 0x0001
    const val AL_BANDPASS_GAINLF = 0x0002
    const val AL_BANDPASS_GAINHF = 0x0003
    const val AL_FILTER_FIRST_PARAMETER = 0x0000
    const val AL_FILTER_LAST_PARAMETER = 0x8000
    const val AL_FILTER_TYPE = 0x8001
    const val AL_FILTER_NULL = 0x0000
    const val AL_FILTER_LOWPASS = 0x0001
    const val AL_FILTER_HIGHPASS = 0x0002
    const val AL_FILTER_BANDPASS = 0x0003

    init {
        System.loadLibrary("openal")
    }

    external fun create(contextAttributes: IntArray? = null): Boolean  // Must be called once, before calling other AL methods!

    external fun destroy()  // Must be called before shutting down the app!

    external fun alAuxiliaryEffectSlotf(
        asid: Int,
        param: Int,
        value: Float
    )

    external fun alAuxiliaryEffectSlotfv(
        asid: Int,
        param: Int,
        values: FloatArray
    )

    external fun alAuxiliaryEffectSloti(
        asid: Int,
        param: Int,
        value: Int
    )

    external fun alAuxiliaryEffectSlotiv(
        asid: Int,
        param: Int,
        values: IntArray
    )

    external fun alBuffer3f(
        bid: Int,
        param: Int,
        value1: Float,
        value2: Float,
        value3: Float
    )

    external fun alBuffer3i(
        bid: Int,
        param: Int,
        value1: Int,
        value2: Int,
        value3: Int
    )

    external fun alBufferData(
        bid: Int,
        format: Int,
        data: ByteArray,
        size: Int,
        freq: Int
    )

    external fun alBufferf(
        bid: Int,
        param: Int,
        value: Float
    )

    external fun alBufferfv(
        bid: Int,
        param: Int,
        values: FloatArray
    )

    external fun alBufferi(
        bid: Int,
        param: Int,
        value: Int
    )

    external fun alBufferiv(
        bid: Int,
        param: Int,
        values: IntArray
    )

    external fun alDeleteAuxiliaryEffectSlots(
        n: Int,
        slots: IntArray
    )

    external fun alDeleteBuffers(
        n: Int,
        buffers: IntArray
    )

    external fun alDeleteEffects(
        n: Int,
        effects: IntArray
    )

    external fun alDeleteFilters(
        n: Int,
        filters: IntArray
    )

    external fun alDeleteSources(
        n: Int,
        sources: IntArray
    )

    external fun alDisable(capability: Int)

    external fun alDistanceModel(distanceModel: Int)

    external fun alDopplerFactor(value: Float)

    external fun alDopplerVelocity(value: Float)

    external fun alEffectf(
        eid: Int,
        param: Int,
        value: Float
    )

    external fun alEffectfv(
        eid: Int,
        param: Int,
        values: FloatArray
    )

    external fun alEffecti(
        eid: Int,
        param: Int,
        value: Int
    )

    external fun alEffectiv(
        eid: Int,
        param: Int,
        values: IntArray
    )

    external fun alEnable(capability: Int)

    external fun alFilterf(
        fid: Int,
        param: Int,
        value: Float
    )

    external fun alFilterfv(
        fid: Int,
        param: Int,
        values: FloatArray
    )

    external fun alFilteri(
        fid: Int,
        param: Int,
        value: Int
    )

    external fun alFilteriv(
        fid: Int,
        param: Int,
        values: IntArray
    )

    external fun alGenAuxiliaryEffectSlots(
        n: Int,
        slots: IntArray
    )

    external fun alGenBuffers(
        n: Int,
        buffers: IntArray
    )

    external fun alGenEffects(
        n: Int,
        effects: IntArray
    )

    external fun alGenFilters(
        n: Int,
        filters: IntArray
    )

    external fun alGenSources(
        n: Int,
        sources: IntArray
    )

    external fun alGetAuxiliaryEffectSlotf(
        asid: Int,
        pname: Int,
        value: FloatArray
    )

    external fun alGetAuxiliaryEffectSlotfv(
        asid: Int,
        pname: Int,
        values: FloatArray
    )

    external fun alGetAuxiliaryEffectSloti(
        asid: Int,
        pname: Int,
        value: IntArray
    )

    external fun alGetAuxiliaryEffectSlotiv(
        asid: Int,
        pname: Int,
        values: IntArray
    )

    external fun alGetBoolean(param: Int): Boolean

    external fun alGetBooleanv(
        param: Int,
        data: ByteArray
    )

    external fun alGetBuffer3f(
        bid: Int,
        param: Int,
        value1: FloatArray,
        value2: FloatArray,
        value3: FloatArray
    )

    external fun alGetBuffer3i(
        bid: Int,
        param: Int,
        value1: IntArray,
        value2: IntArray,
        value3: IntArray
    )

    external fun alGetBufferf(
        bid: Int,
        param: Int,
        value: FloatArray
    )

    external fun alGetBufferfv(
        bid: Int,
        param: Int,
        values: FloatArray
    )

    external fun alGetBufferi(
        bid: Int,
        param: Int,
        value: IntArray
    )

    external fun alGetBufferiv(
        bid: Int,
        param: Int,
        values: IntArray
    )

    external fun alGetDouble(param: Int): Double

    external fun alGetDoublev(
        param: Int,
        data: DoubleArray
    )

    external fun alGetEffectf(
        eid: Int,
        pname: Int,
        value: FloatArray
    )

    external fun alGetEffectfv(
        eid: Int,
        pname: Int,
        values: FloatArray
    )

    external fun alGetEffecti(
        eid: Int,
        pname: Int,
        value: IntArray
    )

    external fun alGetEffectiv(
        eid: Int,
        pname: Int,
        values: IntArray
    )

    external fun alGetEnumValue(ename: String): Int

    external fun alGetError(): Int

    external fun alGetFilterf(
        fid: Int,
        pname: Int,
        value: FloatArray
    )

    external fun alGetFilterfv(
        fid: Int,
        pname: Int,
        values: FloatArray
    )

    external fun alGetFilteri(
        fid: Int,
        pname: Int,
        value: IntArray
    )

    external fun alGetFilteriv(
        fid: Int,
        pname: Int,
        values: IntArray
    )

    external fun alGetFloat(param: Int): Float

    external fun alGetFloatv(
        param: Int,
        data: FloatArray
    )

    external fun alGetInteger(param: Int): Int

    external fun alGetIntegerv(
        param: Int,
        data: IntArray
    )

    external fun alGetListener3f(
        param: Int,
        value1: FloatArray,
        value2: FloatArray,
        value3: FloatArray
    )

    external fun alGetListener3i(
        param: Int,
        value1: IntArray,
        value2: IntArray,
        value3: IntArray
    )

    external fun alGetListenerf(
        param: Int,
        value: FloatArray
    )

    external fun alGetListenerfv(
        param: Int,
        values: FloatArray
    )

    external fun alGetListeneri(
        param: Int,
        value: IntArray
    )

    external fun lGetListeneriv(
        param: Int,
        values: IntArray
    )

    external fun alGetSource3f(
        sid: Int,
        param: Int,
        value1: FloatArray,
        value2: FloatArray,
        value3: FloatArray
    )

    external fun alGetSource3i(
        sid: Int,
        param: Int,
        value1: IntArray,
        value2: IntArray,
        value3: IntArray
    )

    external fun alGetSourcef(
        sid: Int,
        param: Int,
        value: FloatArray
    )

    external fun alGetSourcefv(
        sid: Int,
        param: Int,
        values: FloatArray
    )

    external fun alGetSourcei(
        sid: Int,
        param: Int,
        value: IntArray
    )

    external fun alGetSourceiv(
        sid: Int,
        param: Int,
        values: IntArray
    )

    external fun alGetString(param: Int): String

    external fun alIsAuxiliaryEffectSlot(slot: Int): Boolean

    external fun alIsBuffer(bid: Int): Boolean

    external fun alIsEffect(eid: Int): Boolean

    external fun alIsEnabled(capability: Int): Boolean

    external fun alIsExtensionPresent(extname: String): Boolean

    external fun alIsFilter(fid: Int): Boolean

    external fun alIsSource(sid: Int): Boolean

    external fun alListener3f(
        param: Int,
        value1: Float,
        value2: Float,
        value3: Float
    )

    external fun alListener3i(
        param: Int,
        value1: Int,
        value2: Int,
        value3: Int
    )

    external fun alListenerf(
        param: Int,
        value: Float
    )

    external fun alListenerfv(
        param: Int,
        values: FloatArray
    )

    external fun alListeneri(
        param: Int,
        value: Int
    )

    external fun alListeneriv(
        param: Int,
        values: IntArray
    )

    external fun alSource3f(
        sid: Int,
        param: Int,
        value1: Float,
        value2: Float,
        value3: Float
    )

    external fun alSource3i(
        sid: Int,
        param: Int,
        value1: Int,
        value2: Int,
        value3: Int
    )

    external fun alSourcef(
        sid: Int,
        param: Int,
        value: Float
    )

    external fun alSourcefv(
        sid: Int,
        param: Int,
        values: FloatArray
    )

    external fun alSourcei(
        sid: Int,
        param: Int,
        value: Int
    )

    external fun alSourceiv(
        sid: Int,
        param: Int,
        values: IntArray
    )

    external fun alSourcePause(sid: Int)

    external fun alSourcePausev(
        ns: Int,
        sids: IntArray
    )

    external fun alSourcePlay(sid: Int)

    external fun alSourcePlayv(
        ns: Int,
        sids: IntArray
    )

    external fun alSourceQueueBuffers(
        sid: Int,
        numEntries: Int,
        bids: IntArray
    )

    external fun alSourceRewind(sid: Int)

    external fun alSourceRewindv(
        ns: Int,
        sids: IntArray
    )

    external fun alSourceStop(sid: Int)

    external fun alSourceStopv(
        ns: Int,
        sids: IntArray
    )

    external fun alSourceUnqueueBuffers(
        sid: Int,
        numEntries: Int,
        bids: IntArray
    )

    external fun alSpeedOfSound(value: Float)
}

