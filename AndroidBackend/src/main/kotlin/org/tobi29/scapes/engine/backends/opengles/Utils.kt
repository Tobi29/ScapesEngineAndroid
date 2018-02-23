package org.tobi29.scapes.engine.backends.opengles

import org.tobi29.io._clear
import org.tobi29.io._limit
import org.tobi29.io._position
import org.tobi29.stdex.ThreadLocal
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

@PublishedApi
internal val BUFFERS by ThreadLocal {
    Buffers(
        ByteBuffer.allocateDirect(16).order(
            ByteOrder.nativeOrder()
        ).asIntBuffer()
    )
}

@PublishedApi
internal class Buffers(val i4at0: IntBuffer) {
    val i3at0 = i4at0.slice(0, 3)
    val i2at0 = i4at0.slice(0, 2)
    val i2at2 = i4at0.slice(2, 4)
    val i1at0 = i4at0.slice(0, 1)
    val i1at1 = i4at0.slice(1, 2)
    val i1at2 = i4at0.slice(2, 3)
    val i1at3 = i4at0.slice(3, 4)
}

private fun IntBuffer.slice(
    start: Int,
    end: Int
): IntBuffer {
    _position(start)
    _limit(end)
    val slice = slice()
    _clear()
    return slice
}

@PublishedApi
internal inline fun <R> intBuffers(block: (IntBuffer) -> R): R {
    val buffers = BUFFERS
    val i0 = buffers.i1at0
    try {
        return block(i0)
    } finally {
        i0._clear()
    }
}

@PublishedApi
internal inline fun <R> intBuffers(block: (IntBuffer, IntBuffer) -> R): R {
    val buffers = BUFFERS
    val i0 = buffers.i1at0
    val i1 = buffers.i1at1
    try {
        return block(i0, i1)
    } finally {
        i0._clear()
        i1._clear()
    }
}

@PublishedApi
internal inline fun <R> intBuffers(block: (IntBuffer, IntBuffer, IntBuffer) -> R): R {
    val buffers = BUFFERS
    val i0 = buffers.i1at0
    val i1 = buffers.i1at1
    val i2 = buffers.i1at2
    try {
        return block(i0, i1, i2)
    } finally {
        i0._clear()
        i1._clear()
        i2._clear()
    }
}

@PublishedApi
internal inline fun <R> intBuffers(block: (IntBuffer, IntBuffer, IntBuffer, IntBuffer) -> R): R {
    val buffers = BUFFERS
    val i0 = buffers.i1at0
    val i1 = buffers.i1at1
    val i2 = buffers.i1at2
    val i3 = buffers.i1at3
    try {
        return block(i0, i1, i2, i3)
    } finally {
        i0._clear()
        i1._clear()
        i2._clear()
        i3._clear()
    }
}

@PublishedApi
internal inline fun <R> intBuffers2(block: (IntBuffer) -> R): R {
    val buffers = BUFFERS
    val i0 = buffers.i2at0
    try {
        return block(i0)
    } finally {
        i0._clear()
    }
}

@PublishedApi
internal inline fun <R> intBuffers2(block: (IntBuffer, IntBuffer) -> R): R {
    val buffers = BUFFERS
    val i0 = buffers.i2at0
    val i1 = buffers.i2at2
    try {
        return block(i0, i1)
    } finally {
        i0._clear()
        i1._clear()
    }
}

@PublishedApi
internal inline fun <R> intBuffers3(block: (IntBuffer) -> R): R {
    val buffers = BUFFERS
    val i0 = buffers.i3at0
    try {
        return block(i0)
    } finally {
        i0._clear()
    }
}

@PublishedApi
internal inline fun <R> intBuffers4(block: (IntBuffer) -> R): R {
    val buffers = BUFFERS
    val i0 = buffers.i4at0
    try {
        return block(i0)
    } finally {
        i0._clear()
    }
}

@PublishedApi
internal inline fun <R> intBuffers(
    v0: Int,
    block: (IntBuffer) -> R
): R {
    intBuffers { i0 ->
        i0.put(0, v0)
        return block(i0)
    }
}

@PublishedApi
internal inline fun <R> intBuffers(
    v0: Int,
    v1: Int,
    block: (IntBuffer, IntBuffer) -> R
): R {
    intBuffers { i0, i1 ->
        i0.put(0, v0)
        i1.put(0, v1)
        return block(i0, i1)
    }
}

@PublishedApi
internal inline fun <R> intBuffers(
    v0: Int,
    v1: Int,
    v2: Int,
    block: (IntBuffer, IntBuffer, IntBuffer) -> R
): R {
    intBuffers { i0, i1, i2 ->
        i0.put(0, v0)
        i1.put(0, v1)
        i2.put(0, v2)
        return block(i0, i1, i2)
    }
}

@PublishedApi
internal inline fun <R> intBuffers(
    v0: Int,
    v1: Int,
    v2: Int,
    v3: Int,
    block: (IntBuffer, IntBuffer, IntBuffer, IntBuffer) -> R
): R {
    intBuffers { i0, i1, i2, i3 ->
        i0.put(0, v0)
        i1.put(0, v1)
        i2.put(0, v2)
        i3.put(0, v3)
        return block(i0, i1, i2, i3)
    }
}

@PublishedApi
internal inline fun readInts(block: (IntBuffer) -> Unit) =
    intBuffers { i0 ->
        block(i0)
        i0[0]
    }

@PublishedApi
internal inline fun readInts(block: (IntBuffer, IntBuffer) -> Unit) =
    intBuffers { i0, i1 ->
        block(i0, i1)
        Pair(i0[0], i1[0])
    }

@PublishedApi
internal inline fun readInts(block: (IntBuffer, IntBuffer, IntBuffer) -> Unit) =
    intBuffers { i0, i1, i2 ->
        block(i0, i1, i2)
        Triple(i0[0], i1[0], i2[0])
    }
