package org.tobi29.scapes.engine.android.parcel

import android.os.Parcel
import android.os.Parcelable
import org.tobi29.scapes.engine.utils.ThreadLocal
import org.tobi29.scapes.engine.utils.io.ByteBufferStream
import org.tobi29.scapes.engine.utils.io.tag.binary.readBinary
import org.tobi29.scapes.engine.utils.io.tag.binary.writeBinary
import org.tobi29.scapes.engine.utils.tag.TagMap
import java.nio.ByteBuffer

fun readParcel(parcel: Parcel) = streamRead(parcel, ::readBinary)

fun readParcel(parcel: Parcel,
               allocationLimit: Int = Int.MAX_VALUE,
               compressionStream: ByteBufferStream) =
        streamRead(parcel) { stream ->
            readBinary(stream, allocationLimit, compressionStream)
        }

fun TagMap.writeParcel(parcel: Parcel,
                       compression: Byte = -1,
                       useDictionary: Boolean = true) =
        streamWrite(parcel) { stream ->
            writeBinary(stream, compression, useDictionary)
        }

fun TagMap.writeParcel(parcel: Parcel,
                       compression: Byte = -1,
                       useDictionary: Boolean = true,
                       byteStream: ByteBufferStream) =
        streamWrite(parcel) { stream ->
            writeBinary(stream, compression, useDictionary, byteStream)
        }

fun TagMap.writeParcel(parcel: Parcel,
                       compression: Byte = -1,
                       useDictionary: Boolean = true,
                       byteStream: ByteBufferStream,
                       compressionStream: ByteBufferStream) =
        streamWrite(parcel) { stream ->
            writeBinary(stream, compression, useDictionary, byteStream,
                    compressionStream)
        }

private inline fun <R> streamRead(parcel: Parcel,
                                  block: (ByteBufferStream) -> R): R {
    return block(ByteBufferStream(ByteBuffer.wrap(parcel.createByteArray())))
}

private inline fun <R> streamWrite(parcel: Parcel,
                                   block: (ByteBufferStream) -> R) = stream { stream ->
    val result = block(stream)
    val buffer = stream.buffer()
    buffer.flip()
    assert(buffer.hasArray())
    parcel.writeByteArray(buffer.array(), buffer.arrayOffset(),
            buffer.remaining())
    result
}

private inline fun <R> stream(block: (ByteBufferStream) -> R): R {
    val stream = DATA_STREAM.get()
    try {
        return block(stream)
    } finally {
        stream.buffer().clear()
    }
}

class TagMapParcel(val map: TagMap) : Parcelable {
    override fun writeToParcel(dest: Parcel,
                               flags: Int) {
        map.writeParcel(dest)
    }

    override fun describeContents() = 0

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<TagMapParcel> {
            override fun newArray(size: Int) =
                    arrayOfNulls<TagMapParcel>(size)

            override fun createFromParcel(source: Parcel) = TagMapParcel(
                    readParcel(source)
            )
        }
    }
}

private val DATA_STREAM = ThreadLocal {
    ByteBufferStream(growth = { it + 1048576 })
}
