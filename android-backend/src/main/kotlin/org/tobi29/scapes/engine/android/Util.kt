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

import android.annotation.TargetApi
import android.app.Fragment
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.DisplayMetrics
import android.view.Display
import android.view.InputDevice
import org.tobi29.io.BufferedReadChannelStream
import org.tobi29.io.ReadableByteStream
import org.tobi29.io.filesystem.FilePath
import org.tobi29.io.filesystem.path
import org.tobi29.io.toChannel
import org.tobi29.io.use
import org.tobi29.stdex.InlineUtility
import org.tobi29.stdex.ThreadLocal
import java.nio.channels.Channels

inline val Context.filesPath: FilePath get() = path(filesDir)

inline val Context.cachePath: FilePath get() = path(cacheDir)

inline val Fragment._context: Context?
    get() = if (android.os.Build.VERSION.SDK_INT >= 23) context
    else activity

fun openFileIntent(
    types: Array<String>?,
    multiple: Boolean
) = Intent(
    Intent.ACTION_GET_CONTENT
).apply {
    if (android.os.Build.VERSION.SDK_INT >= 18) {
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple)
    }
    this.type = "*/*"
    if (android.os.Build.VERSION.SDK_INT >= 19) {
        types?.let { putExtra(Intent.EXTRA_MIME_TYPES, it) }
    }
}

fun acceptFile(
    contentResolver: ContentResolver,
    consumer: (String, ReadableByteStream) -> Unit,
    data: Intent
) {
    val file = data.data
    if (file == null) {
        val clipData = data.clipData
        if (clipData != null) {
            val count = clipData.itemCount
            for (i in 0..count - 1) {
                acceptFile(
                    contentResolver,
                    consumer, clipData.getItemAt(i).uri
                )
            }
        }
    } else {
        acceptFile(contentResolver, consumer, file)
    }
}

fun acceptFile(
    contentResolver: ContentResolver,
    consumer: (String, ReadableByteStream) -> Unit,
    file: Uri
) {
    contentResolver.openInputStream(file)?.use { stream ->
        contentResolver.query(
            file, null, null, null,
            null
        )?.use { cursor ->
            cursor.moveToFirst()
            val name = cursor.getString(
                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            )
            consumer.invoke(
                name,
                BufferedReadChannelStream(
                    Channels.newChannel(stream).toChannel()
                )
            )
        }
    }
}

@InlineUtility
@Suppress("NOTHING_TO_INLINE")
inline fun InputDevice.isType(type: Int) = sources and type == type

@InlineUtility
@Suppress("NOTHING_TO_INLINE")
inline val InputDevice.isFullKeyboard
    get() = isType(InputDevice.SOURCE_KEYBOARD)
            && keyboardType == InputDevice.KEYBOARD_TYPE_ALPHABETIC

@TargetApi(26)
fun Context.registerNotificationChannel(
    id: String,
    name: String,
    description: String,
    importance: Int = NotificationManager.IMPORTANCE_DEFAULT
): String {
    val channel = NotificationChannel(id, name, importance)
    channel.description = description
    return registerNotificationChannel(channel)
}

@TargetApi(26)
fun Context.registerNotificationChannel(channel: NotificationChannel): String {
    val notificationManager = getSystemService(
        Context.NOTIFICATION_SERVICE
    ) as NotificationManager
    notificationManager.createNotificationChannel(channel)
    return channel.id
}

private val DISPLAY_METRICS by ThreadLocal { DisplayMetrics() }

val Display.displayMetrics
    get() = DISPLAY_METRICS.also { getMetrics(it) }

val Display.realDisplayMetrics
    get() = DISPLAY_METRICS.also { getRealMetrics(it) }
