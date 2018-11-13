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

import android.os.Bundle
import org.tobi29.io.IOException
import org.tobi29.io.readAsByteArray
import org.tobi29.io.tag.*
import org.tobi29.stdex.ArrayDeque

class TagStructureWriterBundle(rootBundle: Bundle) : TagStructureWriter {
    private val stack = ArrayDeque<Pair<String?, Any>>()
    private var _bundle: Any = rootBundle

    private var bundle
        get() = _bundle as? Bundle
                ?: error("Key operation when list")
        set(value) {
            _bundle = value
        }

    private var list
        get() = _bundle as? BundleList
                ?: error("Non-key operation when not list")
        set(value) {
            _bundle = value
        }

    override fun begin(root: TagMap) {}

    override fun end() {}

    override fun beginStructure(key: String) {
        val bundle = bundle
        stack.push(Pair(key, bundle))
        this.bundle = Bundle()
    }

    override fun beginStructure() {
        val list = list
        stack.push(Pair(null, list))
        this.bundle = Bundle()
    }

    override fun endStructure() {
        val child = Bundle().apply {
            putString("Type", "Map")
            putBundle("Value", bundle)
        }
        val (key, parent) = stack.pop()
        _bundle = parent
        if (key != null) {
            val bundle = bundle
            bundle.putBundle(key, child)
        } else {
            val list = list
            list.addBundle(child)
        }
    }

    override fun structureEmpty(key: String) {
        val bundle = bundle
        bundle.putBundle(key, Bundle().apply {
            putString("Type", "Map")
        })
    }

    override fun structureEmpty() {
        val list = list
        list.addBundle(Bundle().apply {
            putString("Type", "Map")
        })
    }

    override fun beginList(key: String) {
        val bundle = bundle
        stack.push(Pair(key, bundle))
        this.list = BundleList()
    }

    override fun beginList() {
        val list = list
        stack.push(Pair(null, list))
        this.list = BundleList()
    }

    override fun beginListStructure() {
        beginList()
    }

    override fun endListWithTerminate() {
        endStructure()
        endList()
    }

    override fun endListWithEmpty() {
        listEmpty()
        endList()
    }

    override fun endList() {
        val child = list.bundle
        val (key, parent) = stack.pop()
        _bundle = parent
        if (key != null) {
            val bundle = bundle
            bundle.putBundle(key, child)
        } else {
            val list = list
            list.addBundle(child)
        }
    }

    override fun listEmpty(key: String) {
        val bundle = bundle
        bundle.putBundle(key, Bundle().apply {
            putString("Type", "List")
        })
    }

    override fun listEmpty() {
        val list = list
        list.addBundle(Bundle().apply {
            putString("Type", "List")
        })
    }

    override fun writePrimitiveTag(
        key: String,
        tag: TagPrimitive
    ) {
        val bundle = bundle
        when (tag) {
            TagUnit -> bundle.putBundle(key, Bundle().apply {
                putString("Type", "Unit")
            })
            is TagBoolean -> bundle.putBoolean(key, tag.value)
            is TagByte -> bundle.putByte(key, tag.value)
            is TagShort -> bundle.putShort(key, tag.value)
            is TagInt -> bundle.putInt(key, tag.value)
            is TagLong -> bundle.putLong(key, tag.value)
            is TagInteger -> bundle.putLong(key, tag.value.toLong())
            is TagFloat -> bundle.putFloat(key, tag.value)
            is TagDouble -> bundle.putDouble(key, tag.value)
            is TagDecimal -> bundle.putDouble(key, tag.value.toDouble())
            is TagString -> bundle.putString(key, tag.value)
            is TagByteArray -> bundle.putByteArray(key, tag.value.readAsByteArray())
            else -> throw IOException("Invalid type: ${tag::class}")
        }
    }

    override fun writePrimitiveTag(tag: TagPrimitive) {
        val list = list
        when (tag) {
            TagUnit -> list.addBundle(Bundle().apply {
                putString("Type", "Unit")
            })
            is TagBoolean -> list.addBoolean(tag.value)
            is TagByte -> list.addByte(tag.value)
            is TagShort -> list.addShort(tag.value)
            is TagInt -> list.addInt(tag.value)
            is TagLong -> list.addLong(tag.value)
            is TagInteger -> list.addLong(tag.value.toLong())
            is TagFloat -> list.addFloat(tag.value)
            is TagDouble -> list.addDouble(tag.value)
            is TagDecimal -> list.addDouble(tag.value.toDouble())
            is TagString -> list.addString(tag.value)
            is TagByteArray -> list.addByteArray(tag.value.readAsByteArray())
            else -> throw IOException("Invalid type: ${tag::class}")
        }
    }

    private class BundleList {
        val bundle = Bundle().apply {
            putString("Type", "List")
        }

        var i = 0
        private fun next(): String {
            val key = "Element-$i"
            i++
            return key
        }

        fun addBoolean(value: Boolean) {
            bundle.putBoolean(next(), value)
        }

        fun addByte(value: Byte) {
            bundle.putByte(next(), value)
        }

        fun addByteArray(value: ByteArray) {
            bundle.putByteArray(next(), value)
        }

        fun addShort(value: Short) {
            bundle.putShort(next(), value)
        }

        fun addInt(value: Int) {
            bundle.putInt(next(), value)
        }

        fun addLong(value: Long) {
            bundle.putLong(next(), value)
        }

        fun addFloat(value: Float) {
            bundle.putFloat(next(), value)
        }

        fun addDouble(value: Double) {
            bundle.putDouble(next(), value)
        }

        fun addString(value: String) {
            bundle.putString(next(), value)
        }

        fun addBundle(value: Bundle) {
            bundle.putBundle(next(), value)
        }
    }
}
