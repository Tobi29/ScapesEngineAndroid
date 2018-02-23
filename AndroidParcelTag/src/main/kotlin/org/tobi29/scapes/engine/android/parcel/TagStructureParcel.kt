package org.tobi29.scapes.engine.android.parcel

import android.os.Parcel
import android.os.Parcelable
import org.tobi29.io.*
import org.tobi29.io.tag.TagMap
import org.tobi29.io.tag.binary.readBinary
import org.tobi29.io.tag.binary.writeBinary
import org.tobi29.stdex.ThreadLocal

fun readParcel(parcel: Parcel) = streamRead(parcel, ::readBinary)

fun readParcel(
    parcel: Parcel,
    allocationLimit: Int = Int.MAX_VALUE,
    compressionStream: MemoryViewStream<*>
) = streamRead(parcel) { stream ->
    readBinary(stream, allocationLimit, compressionStream)
}

fun TagMap.writeParcel(
    parcel: Parcel,
    compression: Byte = -1,
    useDictionary: Boolean = true
) = streamWrite(parcel) { stream ->
    writeBinary(stream, compression, useDictionary)
}

fun TagMap.writeParcel(
    parcel: Parcel,
    compression: Byte = -1,
    useDictionary: Boolean = true,
    byteStream: MemoryViewStream<*>
) = streamWrite(parcel) { stream ->
    writeBinary(stream, compression, useDictionary, byteStream)
}

fun TagMap.writeParcel(
    parcel: Parcel,
    compression: Byte = -1,
    useDictionary: Boolean = true,
    byteStream: MemoryViewStream<*>,
    compressionStream: MemoryViewStream<*>
) = streamWrite(parcel) { stream ->
    writeBinary(
        stream, compression, useDictionary, byteStream,
        compressionStream
    )
}

private inline fun <R> streamRead(
    parcel: Parcel,
    block: (ReadableByteStream) -> R
): R {
    return block(MemoryViewReadableStream(parcel.createByteArray().viewBE))
}

private inline fun <R> streamWrite(
    parcel: Parcel,
    block: (MemoryViewStream<*>) -> R
) = stream { stream ->
    val result = block(stream)
    stream.flip()
    val buffer = stream.buffer()
    parcel.writeByteArray(buffer.array, buffer.offset, buffer.size)
    result
}

private inline fun <R> stream(block: (MemoryViewStream<HeapViewByte>) -> R): R {
    val stream = DATA_STREAM.get()
    try {
        return block(stream)
    } finally {
        stream.reset()
    }
}

class TagMapParcel(val map: TagMap) : Parcelable {
    override fun writeToParcel(
        dest: Parcel,
        flags: Int
    ) {
        map.writeParcel(dest)
    }

    override fun describeContents() = 0

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<TagMapParcel> {
            override fun newArray(size: Int) =
                arrayOfNulls<TagMapParcel>(size)

            override fun createFromParcel(source: Parcel) = TagMapParcel(
                readParcel(source)
            )
        }
    }
}

private val DATA_STREAM = ThreadLocal { MemoryViewStreamDefault() }
