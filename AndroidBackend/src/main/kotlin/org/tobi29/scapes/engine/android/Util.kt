package org.tobi29.scapes.engine.android

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.DisplayMetrics
import android.view.Display
import android.view.InputDevice
import org.tobi29.scapes.engine.input.FileType
import org.tobi29.scapes.engine.utils.ThreadLocal
import org.tobi29.scapes.engine.utils.io.BufferedReadChannelStream
import org.tobi29.scapes.engine.utils.io.ReadableByteStream
import org.tobi29.scapes.engine.utils.io.filesystem.path
import java.nio.channels.Channels

val Context.filesPath get() = path(filesDir.toString())

val Context.cachePath get() = path(cacheDir.toString())

fun openFileIntent(type: FileType,
                   multiple: Boolean) = Intent(
        Intent.ACTION_GET_CONTENT).apply {
    if (android.os.Build.VERSION.SDK_INT >= 18) {
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple)
    }
    this.type = "*/*"
    if (android.os.Build.VERSION.SDK_INT >= 19) {
        if (type == FileType.IMAGE) {
            val types = arrayOf("image/png")
            putExtra(Intent.EXTRA_MIME_TYPES, types)
        } else if (type == FileType.MUSIC) {
            val types = arrayOf("audio/mpeg", "audio/x-wav",
                    "application/ogg")
            putExtra(Intent.EXTRA_MIME_TYPES, types)
        }
    }
}

fun acceptFile(contentResolver: ContentResolver,
               consumer: (String, ReadableByteStream) -> Unit,
               data: Intent) {
    val file = data.data
    if (file == null) {
        val clipData = data.clipData
        if (clipData != null) {
            val count = clipData.itemCount
            for (i in 0..count - 1) {
                acceptFile(contentResolver,
                        consumer, clipData.getItemAt(i).uri)
            }
        }
    } else {
        acceptFile(contentResolver, consumer, file)
    }
}

fun acceptFile(contentResolver: ContentResolver,
               consumer: (String, ReadableByteStream) -> Unit,
               file: Uri) {
    contentResolver.openInputStream(file)?.use { stream ->
        contentResolver.query(file, null, null, null,
                null)?.use { cursor ->
            cursor.moveToFirst()
            val name = cursor.getString(
                    cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            consumer.invoke(name,
                    BufferedReadChannelStream(
                            Channels.newChannel(stream)))
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun InputDevice.isType(type: Int) = sources and type == type

private val DISPLAY_METRICS = ThreadLocal { DisplayMetrics() }

val Display.displayMetrics get() =
DISPLAY_METRICS.get().also { getMetrics(it) }

val Display.realDisplayMetrics get() =
DISPLAY_METRICS.get().also { getRealMetrics(it) }
