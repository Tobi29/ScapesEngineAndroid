package org.tobi29.scapes.engine.android

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import org.tobi29.scapes.engine.input.FileType
import org.tobi29.scapes.engine.utils.io.BufferedReadChannelStream
import org.tobi29.scapes.engine.utils.io.ReadableByteStream
import org.tobi29.scapes.engine.utils.io.filesystem.path
import java.io.IOException
import java.nio.channels.Channels

val Context.filesPath get() = path(filesDir.toString())

val Context.cachePath get() = path(cacheDir.toString())

fun openFileIntent(type: FileType,
                   multiple: Boolean) = Intent(
        Intent.ACTION_GET_CONTENT).apply {
    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple)
    this.type = "*/*"
    if (type == FileType.IMAGE) {
        val types = arrayOf("image/png")
        putExtra(Intent.EXTRA_MIME_TYPES, types)
    } else if (type == FileType.MUSIC) {
        val types = arrayOf("audio/mpeg", "audio/x-wav",
                "application/ogg")
        putExtra(Intent.EXTRA_MIME_TYPES, types)
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
    try {
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
    } catch (e: IOException) {
        ScapesEngineActivity.logger.error(e) { "Failed to apply picked file" }
    }

}
