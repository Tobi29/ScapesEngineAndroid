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

package org.tobi29.scapes.engine.android.parcel

import android.os.Bundle
import org.tobi29.scapes.engine.utils.tag.*

internal fun Bundle.readMap() = TagMap { readMap(this) }

internal fun Bundle.readMap(map: MutableMap<String, Tag>) {
    for (key in keySet()) {
        get(key)?.bundleAnyToTag()?.let { map[key] = it }
    }
}

internal fun Bundle.readExternalMap() = TagMap { readMap(this) }

internal fun Bundle.readExternalMap(map: MutableMap<String, Tag>) {
    for (key in keySet()) {
        get(key)?.externalAnyToTag()?.let { map[key] = it }
    }
}

internal fun Bundle.readList() = TagList { readList(this) }

internal fun Bundle.readList(list: MutableList<Tag>) {
    var i = 0
    while (true) {
        (get("Element-$i")?.bundleAnyToTag() ?: break).let { list.add(it) }
        i++
    }
}

internal fun Bundle.readExternalList() = TagList { readList(this) }

internal fun Bundle.readExternalList(list: MutableList<Tag>) {
    var i = 0
    while (true) {
        (get("Element-$i")?.externalAnyToTag() ?: break).let { list.add(it) }
        i++
    }
}

private fun Any.primitiveAnyToTag() = when (this) {
    is Byte -> toTag()
    is ByteArray -> toTag()
    is Short -> toTag()
    is ShortArray -> TagList {
        this@primitiveAnyToTag.forEach { add(it.toTag()) }
    }
    is Int -> toTag()
    is IntArray -> TagList {
        this@primitiveAnyToTag.forEach { add(it.toTag()) }
    }
    is Long -> toTag()
    is LongArray -> TagList {
        this@primitiveAnyToTag.forEach { add(it.toTag()) }
    }
    is Float -> toTag()
    is FloatArray -> TagList {
        this@primitiveAnyToTag.forEach { add(it.toTag()) }
    }
    is Double -> toTag()
    is DoubleArray -> TagList {
        this@primitiveAnyToTag.forEach { add(it.toTag()) }
    }
    is String -> toTag()
    else -> null
}

private fun Any.bundleAnyToTag(): Tag? = when (this) {
    is Bundle -> when (getString("Type")) {
        "Unit" -> TagUnit
        "Map" -> getBundle("Value")?.let(Bundle::readMap)
        "List" -> readList()
        else -> null
    }
    is Array<*> -> TagList {
        this@bundleAnyToTag.forEach {
            it?.bundleAnyToTag()?.let { add(it) }
        }
    }
    else -> primitiveAnyToTag()
}

private fun Any.externalAnyToTag(): Tag? = when (this) {
    is Bundle -> readMap()
    is Array<*> -> TagList {
        this@externalAnyToTag.forEach {
            it?.externalAnyToTag()?.let { add(it) }
        }
    }
    else -> primitiveAnyToTag()
}
