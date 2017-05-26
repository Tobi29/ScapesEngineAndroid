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

package org.tobi29.scapes.engine.android

import android.util.SparseArray
import android.view.KeyEvent
import android.view.MotionEvent
import org.tobi29.scapes.engine.input.ControllerKey

object AndroidKeyMap {
    private val KEYS = SparseArray<ControllerKey?>()
    private val BUTTONS = SparseArray<ControllerKey?>()

    init {
        // KEYS.put(KeyEvent.KEYCODE_UNKNOWN, null)
        KEYS.put(KeyEvent.KEYCODE_SOFT_LEFT, c("SOFT_LEFT", "Soft Left"))
        KEYS.put(KeyEvent.KEYCODE_SOFT_RIGHT, c("SOFT_RIGHT", "Soft Right"))
        KEYS.put(KeyEvent.KEYCODE_HOME, c("HOME", "Home"))
        KEYS.put(KeyEvent.KEYCODE_BACK, c("BACK", "Back"))
        KEYS.put(KeyEvent.KEYCODE_CALL, c("CALL", "Call"))
        KEYS.put(KeyEvent.KEYCODE_ENDCALL, c("ENDCALL", "Endcall"))
        KEYS.put(KeyEvent.KEYCODE_0, ControllerKey.KEY_0)
        KEYS.put(KeyEvent.KEYCODE_1, ControllerKey.KEY_1)
        KEYS.put(KeyEvent.KEYCODE_2, ControllerKey.KEY_2)
        KEYS.put(KeyEvent.KEYCODE_3, ControllerKey.KEY_3)
        KEYS.put(KeyEvent.KEYCODE_4, ControllerKey.KEY_4)
        KEYS.put(KeyEvent.KEYCODE_5, ControllerKey.KEY_5)
        KEYS.put(KeyEvent.KEYCODE_6, ControllerKey.KEY_6)
        KEYS.put(KeyEvent.KEYCODE_7, ControllerKey.KEY_7)
        KEYS.put(KeyEvent.KEYCODE_8, ControllerKey.KEY_8)
        KEYS.put(KeyEvent.KEYCODE_9, ControllerKey.KEY_9)
        KEYS.put(KeyEvent.KEYCODE_STAR, c("STAR", "Star"))
        KEYS.put(KeyEvent.KEYCODE_POUND, c("POUND", "Pound"))
        KEYS.put(KeyEvent.KEYCODE_DPAD_UP, ControllerKey.KEY_UP)
        KEYS.put(KeyEvent.KEYCODE_DPAD_DOWN, ControllerKey.KEY_DOWN)
        KEYS.put(KeyEvent.KEYCODE_DPAD_LEFT, ControllerKey.KEY_LEFT)
        KEYS.put(KeyEvent.KEYCODE_DPAD_RIGHT, ControllerKey.KEY_RIGHT)
        KEYS.put(KeyEvent.KEYCODE_DPAD_CENTER, c("DPAD_CENTER", "DPad Center"))
        KEYS.put(KeyEvent.KEYCODE_VOLUME_UP, c("VOLUME_UP", "Volume Up"))
        KEYS.put(KeyEvent.KEYCODE_VOLUME_DOWN, c("VOLUME_DOWN", "Volume Down"))
        KEYS.put(KeyEvent.KEYCODE_POWER, c("POWER", "Power"))
        KEYS.put(KeyEvent.KEYCODE_CAMERA, c("CAMERA", "Camera"))
        KEYS.put(KeyEvent.KEYCODE_CLEAR, c("CLEAR", "Clear"))
        KEYS.put(KeyEvent.KEYCODE_A, ControllerKey.KEY_A)
        KEYS.put(KeyEvent.KEYCODE_B, ControllerKey.KEY_B)
        KEYS.put(KeyEvent.KEYCODE_C, ControllerKey.KEY_C)
        KEYS.put(KeyEvent.KEYCODE_D, ControllerKey.KEY_D)
        KEYS.put(KeyEvent.KEYCODE_E, ControllerKey.KEY_E)
        KEYS.put(KeyEvent.KEYCODE_F, ControllerKey.KEY_F)
        KEYS.put(KeyEvent.KEYCODE_G, ControllerKey.KEY_G)
        KEYS.put(KeyEvent.KEYCODE_H, ControllerKey.KEY_H)
        KEYS.put(KeyEvent.KEYCODE_I, ControllerKey.KEY_I)
        KEYS.put(KeyEvent.KEYCODE_J, ControllerKey.KEY_J)
        KEYS.put(KeyEvent.KEYCODE_K, ControllerKey.KEY_K)
        KEYS.put(KeyEvent.KEYCODE_L, ControllerKey.KEY_L)
        KEYS.put(KeyEvent.KEYCODE_M, ControllerKey.KEY_M)
        KEYS.put(KeyEvent.KEYCODE_N, ControllerKey.KEY_N)
        KEYS.put(KeyEvent.KEYCODE_O, ControllerKey.KEY_O)
        KEYS.put(KeyEvent.KEYCODE_P, ControllerKey.KEY_P)
        KEYS.put(KeyEvent.KEYCODE_Q, ControllerKey.KEY_Q)
        KEYS.put(KeyEvent.KEYCODE_R, ControllerKey.KEY_R)
        KEYS.put(KeyEvent.KEYCODE_S, ControllerKey.KEY_S)
        KEYS.put(KeyEvent.KEYCODE_T, ControllerKey.KEY_T)
        KEYS.put(KeyEvent.KEYCODE_U, ControllerKey.KEY_U)
        KEYS.put(KeyEvent.KEYCODE_V, ControllerKey.KEY_V)
        KEYS.put(KeyEvent.KEYCODE_W, ControllerKey.KEY_W)
        KEYS.put(KeyEvent.KEYCODE_X, ControllerKey.KEY_X)
        KEYS.put(KeyEvent.KEYCODE_Y, ControllerKey.KEY_Y)
        KEYS.put(KeyEvent.KEYCODE_Z, ControllerKey.KEY_Z)
        KEYS.put(KeyEvent.KEYCODE_COMMA, ControllerKey.KEY_COMMA)
        KEYS.put(KeyEvent.KEYCODE_PERIOD, ControllerKey.KEY_PERIOD)
        KEYS.put(KeyEvent.KEYCODE_ALT_LEFT, ControllerKey.KEY_ALT_LEFT)
        KEYS.put(KeyEvent.KEYCODE_ALT_RIGHT, ControllerKey.KEY_ALT_RIGHT)
        KEYS.put(KeyEvent.KEYCODE_SHIFT_LEFT, ControllerKey.KEY_SHIFT_LEFT)
        KEYS.put(KeyEvent.KEYCODE_SHIFT_RIGHT, ControllerKey.KEY_SHIFT_RIGHT)
        KEYS.put(KeyEvent.KEYCODE_TAB, ControllerKey.KEY_TAB)
        KEYS.put(KeyEvent.KEYCODE_SPACE, ControllerKey.KEY_SPACE)
        KEYS.put(KeyEvent.KEYCODE_SYM, c("SYM", "Sym"))
        KEYS.put(KeyEvent.KEYCODE_EXPLORER, c("EXPLORER", "Explorer"))
        KEYS.put(KeyEvent.KEYCODE_ENVELOPE, c("ENVELOPE", "Envelope"))
        KEYS.put(KeyEvent.KEYCODE_ENTER, ControllerKey.KEY_ENTER)
        KEYS.put(KeyEvent.KEYCODE_DEL, ControllerKey.KEY_DELETE)
        KEYS.put(KeyEvent.KEYCODE_GRAVE, ControllerKey.KEY_GRAVE_ACCENT)
        KEYS.put(KeyEvent.KEYCODE_MINUS, ControllerKey.KEY_MINUS)
        KEYS.put(KeyEvent.KEYCODE_EQUALS, ControllerKey.KEY_EQUAL)
        KEYS.put(KeyEvent.KEYCODE_LEFT_BRACKET, ControllerKey.KEY_BRACKET_LEFT)
        KEYS.put(KeyEvent.KEYCODE_RIGHT_BRACKET,
                ControllerKey.KEY_BRACKET_RIGHT)
        KEYS.put(KeyEvent.KEYCODE_BACKSLASH, ControllerKey.KEY_BACKSLASH)
        KEYS.put(KeyEvent.KEYCODE_SEMICOLON, ControllerKey.KEY_SEMICOLON)
        KEYS.put(KeyEvent.KEYCODE_APOSTROPHE, ControllerKey.KEY_APOSTROPHE)
        KEYS.put(KeyEvent.KEYCODE_SLASH, ControllerKey.KEY_SLASH)
        KEYS.put(KeyEvent.KEYCODE_AT, c("AT", "AT"))
        KEYS.put(KeyEvent.KEYCODE_NUM, c("NUM", "NUM"))
        KEYS.put(KeyEvent.KEYCODE_HEADSETHOOK, c("HEADSETHOOK", "HeadsetHook"))
        KEYS.put(KeyEvent.KEYCODE_FOCUS, c("FOCUS", "Focus"))
        KEYS.put(KeyEvent.KEYCODE_PLUS, c("PLUS", "Plus"))
        KEYS.put(KeyEvent.KEYCODE_MENU, ControllerKey.KEY_MENU)
        KEYS.put(KeyEvent.KEYCODE_NOTIFICATION,
                c("NOTIFICATION", "Notification"))
        KEYS.put(KeyEvent.KEYCODE_SEARCH, c("SEARCH", "Search"))

        if (android.os.Build.VERSION.SDK_INT >= 3) {
            KEYS.put(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
                    c("MEDIA_PLAY_PAUSE", "Media Play Pause"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_STOP, c("MEDIA_STOP", "Media Stop"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_NEXT, c("MEDIA_NEXT", "Media Next"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_PREVIOUS,
                    c("MEDIA_PREVIOUS", "Media Previous"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_REWIND,
                    c("MEDIA_REWIND", "Media Rewind"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_FAST_FORWARD,
                    c("MEDIA_FAST_FORWARD", "Media Fast Forward"))
            KEYS.put(KeyEvent.KEYCODE_MUTE, c("MUTE", "Mute"))
        }

        if (android.os.Build.VERSION.SDK_INT >= 9) {
            KEYS.put(KeyEvent.KEYCODE_PAGE_UP, ControllerKey.KEY_PAGE_UP)
            KEYS.put(KeyEvent.KEYCODE_PAGE_DOWN, ControllerKey.KEY_PAGE_DOWN)
            KEYS.put(KeyEvent.KEYCODE_PICTSYMBOLS,
                    c("PICTSYMBOLS", "PictSymbols"))
            KEYS.put(KeyEvent.KEYCODE_SWITCH_CHARSET,
                    c("SWITCH_CHARSET", "Switch Charset"))
            KEYS.put(KeyEvent.KEYCODE_BUTTON_A, ControllerKey.BUTTON_A)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_B, ControllerKey.BUTTON_B)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_C, c("BUTTON_C", "Button C"))
            KEYS.put(KeyEvent.KEYCODE_BUTTON_X, ControllerKey.BUTTON_X)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_Y, ControllerKey.BUTTON_Y)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_Z, c("BUTTON_Z", "Button Z"))
            KEYS.put(KeyEvent.KEYCODE_BUTTON_L1,
                    ControllerKey.BUTTON_BUMPER_LEFT)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_R1,
                    ControllerKey.BUTTON_BUMPER_RIGHT)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_L2,
                    ControllerKey.BUTTON_TRIGGER_LEFT)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_R2,
                    ControllerKey.BUTTON_TRIGGER_RIGHT)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_THUMBL,
                    ControllerKey.BUTTON_THUMB_LEFT)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_THUMBR,
                    ControllerKey.BUTTON_THUMB_RIGHT)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_START, ControllerKey.BUTTON_START)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_SELECT,
                    ControllerKey.BUTTON_SELECT)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_MODE,
                    ControllerKey.BUTTON_WHATEVER)
        }

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            KEYS.put(KeyEvent.KEYCODE_ESCAPE, ControllerKey.KEY_ESCAPE)
            KEYS.put(KeyEvent.KEYCODE_FORWARD_DEL, ControllerKey.KEY_BACKSPACE)
            KEYS.put(KeyEvent.KEYCODE_CTRL_LEFT, ControllerKey.KEY_CONTROL_LEFT)
            KEYS.put(KeyEvent.KEYCODE_CTRL_RIGHT,
                    ControllerKey.KEY_CONTROL_RIGHT)
            KEYS.put(KeyEvent.KEYCODE_CAPS_LOCK, ControllerKey.KEY_CAPS_LOCK)
            KEYS.put(KeyEvent.KEYCODE_SCROLL_LOCK,
                    ControllerKey.KEY_SCROLL_LOCK)
            KEYS.put(KeyEvent.KEYCODE_META_LEFT, ControllerKey.KEY_SUPER_LEFT)
            KEYS.put(KeyEvent.KEYCODE_META_RIGHT, ControllerKey.KEY_SUPER_RIGHT)
            KEYS.put(KeyEvent.KEYCODE_FUNCTION, c("FUNCTION", "Function"))
            KEYS.put(KeyEvent.KEYCODE_SYSRQ, ControllerKey.KEY_PRINT_SCREEN)
            KEYS.put(KeyEvent.KEYCODE_BREAK, ControllerKey.KEY_PAUSE)
            KEYS.put(KeyEvent.KEYCODE_MOVE_HOME, ControllerKey.KEY_HOME)
            KEYS.put(KeyEvent.KEYCODE_MOVE_END, ControllerKey.KEY_END)
            KEYS.put(KeyEvent.KEYCODE_INSERT, ControllerKey.KEY_INSERT)
            KEYS.put(KeyEvent.KEYCODE_FORWARD, c("FORWARD", "Forward"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_PLAY, c("MEDIA_PLAY", "Media Play"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_PAUSE,
                    c("MEDIA_PAUSE", "Media Pause"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_CLOSE,
                    c("MEDIA_CLOSE", "Media Close"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_EJECT,
                    c("MEDIA_EJECT", "Media Eject"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_RECORD,
                    c("MEDIA_RECORD", "Media Record"))
            KEYS.put(KeyEvent.KEYCODE_F1, ControllerKey.KEY_F1)
            KEYS.put(KeyEvent.KEYCODE_F2, ControllerKey.KEY_F2)
            KEYS.put(KeyEvent.KEYCODE_F3, ControllerKey.KEY_F3)
            KEYS.put(KeyEvent.KEYCODE_F4, ControllerKey.KEY_F4)
            KEYS.put(KeyEvent.KEYCODE_F5, ControllerKey.KEY_F5)
            KEYS.put(KeyEvent.KEYCODE_F6, ControllerKey.KEY_F6)
            KEYS.put(KeyEvent.KEYCODE_F7, ControllerKey.KEY_F7)
            KEYS.put(KeyEvent.KEYCODE_F8, ControllerKey.KEY_F8)
            KEYS.put(KeyEvent.KEYCODE_F9, ControllerKey.KEY_F9)
            KEYS.put(KeyEvent.KEYCODE_F10, ControllerKey.KEY_F10)
            KEYS.put(KeyEvent.KEYCODE_F11, ControllerKey.KEY_F11)
            KEYS.put(KeyEvent.KEYCODE_F12, ControllerKey.KEY_F12)
            KEYS.put(KeyEvent.KEYCODE_NUM_LOCK, ControllerKey.KEY_NUM_LOCK)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_0, ControllerKey.KEY_KP_0)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_1, ControllerKey.KEY_KP_1)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_2, ControllerKey.KEY_KP_2)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_3, ControllerKey.KEY_KP_3)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_4, ControllerKey.KEY_KP_4)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_5, ControllerKey.KEY_KP_5)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_6, ControllerKey.KEY_KP_6)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_7, ControllerKey.KEY_KP_7)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_8, ControllerKey.KEY_KP_8)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_9, ControllerKey.KEY_KP_9)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_DIVIDE,
                    ControllerKey.KEY_KP_DIVIDE)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_MULTIPLY,
                    ControllerKey.KEY_KP_MULTIPLY)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_SUBTRACT,
                    ControllerKey.KEY_KP_SUBTRACT)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_ADD, ControllerKey.KEY_KP_ADD)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_DOT, c("NUMPAD_DOT", "Numpad Dot"))
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_COMMA,
                    c("NUMPAD_COMMA", "Numpad Comma"))
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_ENTER, ControllerKey.KEY_KP_ENTER)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_EQUALS, ControllerKey.KEY_KP_EQUAL)
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_LEFT_PAREN,
                    c("NUMPAD_LEFT_PAREN", "Numpad Left Parenthesis"))
            KEYS.put(KeyEvent.KEYCODE_NUMPAD_RIGHT_PAREN,
                    c("NUMPAD_RIGHT_PAREN", "Numpad Right Parenthesis"))
            KEYS.put(KeyEvent.KEYCODE_VOLUME_MUTE,
                    c("VOLUME_MUTE", "Volume Mute"))
            KEYS.put(KeyEvent.KEYCODE_INFO, c("INFO", "Info"))
            KEYS.put(KeyEvent.KEYCODE_CHANNEL_UP, c("CHANNEL_UP", "Channel Up"))
            KEYS.put(KeyEvent.KEYCODE_CHANNEL_DOWN,
                    c("CHANNEL_DOWN", "Channel Down"))
            KEYS.put(KeyEvent.KEYCODE_ZOOM_IN, c("ZOOM_IN", "Zoom In"))
            KEYS.put(KeyEvent.KEYCODE_ZOOM_OUT, c("ZOOM_OUT", "Zoom Out"))
            KEYS.put(KeyEvent.KEYCODE_TV, c("TV", "TV"))
            KEYS.put(KeyEvent.KEYCODE_WINDOW, c("WINDOW", "Window"))
            KEYS.put(KeyEvent.KEYCODE_GUIDE, c("GUIDE", "Guide"))
            KEYS.put(KeyEvent.KEYCODE_DVR, c("DVR", "DVR"))
            KEYS.put(KeyEvent.KEYCODE_BOOKMARK, c("BOOKMARK", "Bookmark"))
            KEYS.put(KeyEvent.KEYCODE_CAPTIONS, c("CAPTIONS", "Captions"))
            KEYS.put(KeyEvent.KEYCODE_SETTINGS, c("SETTINGS", "Settings"))
            KEYS.put(KeyEvent.KEYCODE_TV_POWER, c("TV_POWER", "TV Power"))
            KEYS.put(KeyEvent.KEYCODE_TV_INPUT, c("TV_INPUT", "TV Input"))
            KEYS.put(KeyEvent.KEYCODE_STB_POWER, c("STB_POWER", "STB Power"))
            KEYS.put(KeyEvent.KEYCODE_STB_INPUT, c("STB_INPUT", "STB Input"))
            KEYS.put(KeyEvent.KEYCODE_AVR_POWER, c("AVR_POWER", "AVR Power"))
            KEYS.put(KeyEvent.KEYCODE_AVR_INPUT, c("AVR_INPUT", "AVR Input"))
            KEYS.put(KeyEvent.KEYCODE_PROG_RED, c("PROG_RED", "Prog Red"))
            KEYS.put(KeyEvent.KEYCODE_PROG_GREEN, c("PROG_GREEN", "Prog Green"))
            KEYS.put(KeyEvent.KEYCODE_PROG_YELLOW,
                    c("PROG_YELLOW", "Prog Yellow"))
            KEYS.put(KeyEvent.KEYCODE_PROG_BLUE, c("PROG_BLUE", "Prog Blue"))
            KEYS.put(KeyEvent.KEYCODE_APP_SWITCH, c("APP_SWITCH", "App Switch"))
        }

        if (android.os.Build.VERSION.SDK_INT >= 12) {
            KEYS.put(KeyEvent.KEYCODE_BUTTON_1, ControllerKey.BUTTON_1)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_2, ControllerKey.BUTTON_2)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_3, ControllerKey.BUTTON_3)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_4, ControllerKey.BUTTON_4)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_5, ControllerKey.BUTTON_5)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_6, ControllerKey.BUTTON_6)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_7, ControllerKey.BUTTON_7)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_8, ControllerKey.BUTTON_8)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_9, ControllerKey.BUTTON_9)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_10, ControllerKey.BUTTON_10)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_11, ControllerKey.BUTTON_11)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_12, ControllerKey.BUTTON_12)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_13, ControllerKey.BUTTON_13)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_14, ControllerKey.BUTTON_14)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_15, ControllerKey.BUTTON_15)
            KEYS.put(KeyEvent.KEYCODE_BUTTON_16, ControllerKey.BUTTON_16)
        }

        if (android.os.Build.VERSION.SDK_INT >= 14) {
            KEYS.put(KeyEvent.KEYCODE_LANGUAGE_SWITCH,
                    c("LANGUAGE_SWITCH", "Language Switch"))
            KEYS.put(KeyEvent.KEYCODE_MANNER_MODE,
                    c("MANNER_MODE", "Manner Mode"))
            KEYS.put(KeyEvent.KEYCODE_3D_MODE, c("3D_MODE", "3D Mode"))
        }

        if (android.os.Build.VERSION.SDK_INT >= 15) {
            KEYS.put(KeyEvent.KEYCODE_CONTACTS, c("CONTACTS", "Contacts"))
            KEYS.put(KeyEvent.KEYCODE_CALENDAR, c("CALENDAR", "Calendar"))
            KEYS.put(KeyEvent.KEYCODE_MUSIC, c("MUSIC", "Music"))
            KEYS.put(KeyEvent.KEYCODE_CALCULATOR, c("CALCULATOR", "Calculator"))
        }

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            KEYS.put(KeyEvent.KEYCODE_ZENKAKU_HANKAKU,
                    c("ZENKAKU_HANKAKU", "Zenkaku Hankaku"))
            KEYS.put(KeyEvent.KEYCODE_EISU, c("EISU", "Eisu"))
            KEYS.put(KeyEvent.KEYCODE_MUHENKAN, c("MUHENKAN", "Muhenkan"))
            KEYS.put(KeyEvent.KEYCODE_HENKAN, c("HENKAN", "Henkan"))
            KEYS.put(KeyEvent.KEYCODE_KATAKANA_HIRAGANA,
                    c("KATAKANA_HIRAGANA", "Katakana Hiragana"))
            KEYS.put(KeyEvent.KEYCODE_YEN, c("YEN", "Yen"))
            KEYS.put(KeyEvent.KEYCODE_RO, c("RO", "Ro"))
            KEYS.put(KeyEvent.KEYCODE_KANA, c("KANA", "Kana"))
            KEYS.put(KeyEvent.KEYCODE_ASSIST, c("ASSIST", "Assist"))
        }

        if (android.os.Build.VERSION.SDK_INT >= 18) {
            KEYS.put(KeyEvent.KEYCODE_BRIGHTNESS_DOWN,
                    c("BRIGHTNESS_DOWN", "Brightness Down"))
            KEYS.put(KeyEvent.KEYCODE_BRIGHTNESS_UP,
                    c("BRIGHTNESS_UP", "Brightness Up"))
        }

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            KEYS.put(KeyEvent.KEYCODE_MEDIA_AUDIO_TRACK,
                    c("MEDIA_AUDIO_TRACK", "Media Audio Track"))
        }

        if (android.os.Build.VERSION.SDK_INT >= 20) {
            KEYS.put(KeyEvent.KEYCODE_SLEEP, c("SLEEP", "Sleep"))
            KEYS.put(KeyEvent.KEYCODE_WAKEUP, c("WAKEUP", "Wakeup"))
        }

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            KEYS.put(KeyEvent.KEYCODE_PAIRING, c("PAIRING", "Pairing"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_TOP_MENU,
                    c("MEDIA_TOP_MENU", "Media Top Menu"))
            KEYS.put(KeyEvent.KEYCODE_11, c("11", "11"))
            KEYS.put(KeyEvent.KEYCODE_12, c("12", "12"))
            KEYS.put(KeyEvent.KEYCODE_LAST_CHANNEL,
                    c("LAST_CHANNEL", "Last Channel"))
            KEYS.put(KeyEvent.KEYCODE_TV_DATA_SERVICE,
                    c("TV_DATA_SERVICE", "TV Data Service"))
            KEYS.put(KeyEvent.KEYCODE_VOICE_ASSIST,
                    c("VOICE_ASSIST", "Voice Assist"))
            KEYS.put(KeyEvent.KEYCODE_TV_RADIO_SERVICE,
                    c("TV_RADIO_SERVICE", "TV Radio Service"))
            KEYS.put(KeyEvent.KEYCODE_TV_TELETEXT,
                    c("TV_TELETEXT", "TV Teletext"))
            KEYS.put(KeyEvent.KEYCODE_TV_NUMBER_ENTRY,
                    c("TV_NUMBER_ENTRY", "TV  Number Entry"))
            KEYS.put(KeyEvent.KEYCODE_TV_TERRESTRIAL_ANALOG,
                    c("TV_TERRESTRIAL_ANALOG", "TV Terrestrial Analog"))
            KEYS.put(KeyEvent.KEYCODE_TV_TERRESTRIAL_DIGITAL,
                    c("TV_TERRESTRIAL_DIGITAL", "TV Terrestrial Digital"))
            KEYS.put(KeyEvent.KEYCODE_TV_SATELLITE,
                    c("TV_SATELLITE", "TV Satellite"))
            KEYS.put(KeyEvent.KEYCODE_TV_SATELLITE_BS,
                    c("TV_SATELLITE_BS", "TV Satellite BS"))
            KEYS.put(KeyEvent.KEYCODE_TV_SATELLITE_CS,
                    c("TV_SATELLITE_CS", "TV Satellite CS"))
            KEYS.put(KeyEvent.KEYCODE_TV_SATELLITE_SERVICE,
                    c("TV_SATELLITE_SERVICE", "TV Satellite Service"))
            KEYS.put(KeyEvent.KEYCODE_TV_NETWORK, c("TV_NETWORK", "TV Network"))
            KEYS.put(KeyEvent.KEYCODE_TV_ANTENNA_CABLE,
                    c("TV_ANTENNA_CABLE", "TV Antenna Cable"))
            KEYS.put(KeyEvent.KEYCODE_TV_INPUT_HDMI_1,
                    c("TV_INPUT_HDMI_1", "TV Input HDMI 1"))
            KEYS.put(KeyEvent.KEYCODE_TV_INPUT_HDMI_2,
                    c("TV_INPUT_HDMI_2", "TV Input HDMI 2"))
            KEYS.put(KeyEvent.KEYCODE_TV_INPUT_HDMI_3,
                    c("TV_INPUT_HDMI_3", "TV Input HDMI 3"))
            KEYS.put(KeyEvent.KEYCODE_TV_INPUT_HDMI_4,
                    c("TV_INPUT_HDMI_4", "TV Input HDMI 4"))
            KEYS.put(KeyEvent.KEYCODE_TV_INPUT_COMPOSITE_1,
                    c("TV_INPUT_COMPOSITE_1", "TV Input Composite 1"))
            KEYS.put(KeyEvent.KEYCODE_TV_INPUT_COMPOSITE_2,
                    c("TV_INPUT_COMPOSITE_2", "TV Input Composite 2"))
            KEYS.put(KeyEvent.KEYCODE_TV_INPUT_COMPONENT_1,
                    c("TV_INPUT_COMPONENT_1", "TV Input Composite 1"))
            KEYS.put(KeyEvent.KEYCODE_TV_INPUT_COMPONENT_2,
                    c("TV_INPUT_COMPONENT_2", "TV Input Composite 2"))
            KEYS.put(KeyEvent.KEYCODE_TV_INPUT_VGA_1,
                    c("TV_INPUT_VGA_1", "TV Input VGA 1"))
            KEYS.put(KeyEvent.KEYCODE_TV_AUDIO_DESCRIPTION,
                    c("TV_AUDIO_DESCRIPTION", "TV Audio Description"))
            KEYS.put(KeyEvent.KEYCODE_TV_AUDIO_DESCRIPTION_MIX_UP,
                    c("TV_AUDIO_DESCRIPTION_MIX_UP",
                            "TV Audio Description Mix Up"))
            KEYS.put(KeyEvent.KEYCODE_TV_AUDIO_DESCRIPTION_MIX_DOWN,
                    c("TV_AUDIO_DESCRIPTION_MIX_DOWN",
                            "TV Audio Description Mix Down"))
            KEYS.put(KeyEvent.KEYCODE_TV_ZOOM_MODE,
                    c("TV_ZOOM_MODE", "TV Zoom Mode"))
            KEYS.put(KeyEvent.KEYCODE_TV_CONTENTS_MENU,
                    c("TV_CONTENTS_MENU", "TV Contents Menu"))
            KEYS.put(KeyEvent.KEYCODE_TV_MEDIA_CONTEXT_MENU,
                    c("TV_MEDIA_CONTEXT_MENU", "TV Media Context Menu"))
            KEYS.put(KeyEvent.KEYCODE_TV_TIMER_PROGRAMMING,
                    c("TV_TIMER_PROGRAMMING", "TV Timer Programming"))
            KEYS.put(KeyEvent.KEYCODE_HELP, c("HELP", "Help"))
        }

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            KEYS.put(KeyEvent.KEYCODE_NAVIGATE_PREVIOUS,
                    c("NAVIGATE_PREVIOUS", "Navigate Previous"))
            KEYS.put(KeyEvent.KEYCODE_NAVIGATE_NEXT,
                    c("NAVIGATE_NEXT", "Navigate Next"))
            KEYS.put(KeyEvent.KEYCODE_NAVIGATE_IN,
                    c("NAVIGATE_IN", "Navigate In"))
            KEYS.put(KeyEvent.KEYCODE_NAVIGATE_OUT,
                    c("NAVIGATE_OUT", "Navigate Out"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_SKIP_FORWARD,
                    c("MEDIA_SKIP_FORWARD", "Media Skip Forward"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_SKIP_BACKWARD,
                    c("MEDIA_SKIP_BACKWARD", "Media Skip Backward"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_STEP_FORWARD,
                    c("MEDIA_STEP_FORWARD", "Media Step Forward"))
            KEYS.put(KeyEvent.KEYCODE_MEDIA_STEP_BACKWARD,
                    c("MEDIA_STEP_BACKWARD", "Media Step Backward"))
        }

        if (android.os.Build.VERSION.SDK_INT >= 24) {
            KEYS.put(KeyEvent.KEYCODE_STEM_PRIMARY,
                    c("STEM_PRIMARY", "Stem Primary"))
            KEYS.put(KeyEvent.KEYCODE_STEM_1, c("STEM_1", "Stem 1"))
            KEYS.put(KeyEvent.KEYCODE_STEM_2, c("STEM_2", "Stem 2"))
            KEYS.put(KeyEvent.KEYCODE_STEM_3, c("STEM_3", "Stem 3"))
            KEYS.put(KeyEvent.KEYCODE_DPAD_UP_LEFT,
                    c("DPAD_UP_LEFT", "DPad Up Left"))
            KEYS.put(KeyEvent.KEYCODE_DPAD_DOWN_LEFT,
                    c("DPAD_DOWN_LEFT", "DPad Down Left"))
            KEYS.put(KeyEvent.KEYCODE_DPAD_UP_RIGHT,
                    c("DPAD_UP_RIGHT", "DPad Up Right"))
            KEYS.put(KeyEvent.KEYCODE_DPAD_DOWN_RIGHT,
                    c("DPAD_DOWN_RIGHT", "DPad Down Right"))
            KEYS.put(KeyEvent.KEYCODE_SOFT_SLEEP, c("SOFT_SLEEP", "Soft Sleep"))
            KEYS.put(KeyEvent.KEYCODE_CUT, c("CUT", "Cut"))
            KEYS.put(KeyEvent.KEYCODE_COPY, c("COPY", "Copy"))
            KEYS.put(KeyEvent.KEYCODE_PASTE, c("PASTE", "Paste"))
        }

        if (android.os.Build.VERSION.SDK_INT >= 25) {
            KEYS.put(KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP,
                    c("SYSTEM_NAVIGATION_UP", "System Navigation Up"))
            KEYS.put(KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN,
                    c("SYSTEM_NAVIGATION_DOWN", "System Navigation Down"))
            KEYS.put(KeyEvent.KEYCODE_SYSTEM_NAVIGATION_LEFT,
                    c("SYSTEM_NAVIGATION_LEFT", "System Navigation Left"))
            KEYS.put(KeyEvent.KEYCODE_SYSTEM_NAVIGATION_RIGHT,
                    c("SYSTEM_NAVIGATION_RIGHT", "System Navigation Right"))
        }

        if (android.os.Build.VERSION.SDK_INT >= 14) {
            BUTTONS.put(MotionEvent.BUTTON_PRIMARY, ControllerKey.BUTTON_0)
            BUTTONS.put(MotionEvent.BUTTON_SECONDARY, ControllerKey.BUTTON_1)
            BUTTONS.put(MotionEvent.BUTTON_TERTIARY, ControllerKey.BUTTON_2)
            BUTTONS.put(MotionEvent.BUTTON_BACK, ControllerKey.BUTTON_3)
            BUTTONS.put(MotionEvent.BUTTON_FORWARD, ControllerKey.BUTTON_4)
        }

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            BUTTONS.put(MotionEvent.BUTTON_STYLUS_PRIMARY,
                    ControllerKey.BUTTON_5)
            BUTTONS.put(MotionEvent.BUTTON_STYLUS_SECONDARY,
                    ControllerKey.BUTTON_6)
        }
    }

    private fun c(name: String,
                  humanName: String) =
            ControllerKey.of("ANDROID_$name", "$humanName (Android)")

    fun key(id: Int) = KEYS[id]

    fun button(id: Int) = BUTTONS[id]

    fun touch() {}
}
