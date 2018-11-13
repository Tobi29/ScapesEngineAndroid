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
): R = block(MemoryViewReadableStream(parcel.createByteArray().viewBE))

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
