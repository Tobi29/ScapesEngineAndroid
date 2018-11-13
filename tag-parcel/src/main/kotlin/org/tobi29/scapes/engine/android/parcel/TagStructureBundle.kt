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
import org.tobi29.io.tag.TagMap
import org.tobi29.io.tag.write

/**
 * Convert a [Bundle] into a [TagMap] produced by [toBundle]
 *
 * **Note:** This is not meant for generic bundles from anywhere in Android
 * @receiver The [Bundle] to read
 * @return A [TagMap] containing all compatible data from the given [Bundle]
 */
fun Bundle.toTag() = readMap()

/**
 * Convert a [Bundle] into a [TagMap] by attempting to map as much information
 * directly from the [Bundle] into a valid [TagMap]
 *
 * **Note:** This will not correctly reproduce structures from [toBundle]
 * @receiver The [Bundle] to read
 * @return A [TagMap] containing all compatible data from the given [Bundle]
 */
fun Bundle.toTagFromExternal() = readExternalMap()

/**
 * Convert a [TagMap] into a [Bundle] to serialize it using the Android platform
 *
 * **Note:** The resulting bundle contains some metadata because not all
 * structure can be represented in a [Bundle]
 * @receiver The [TagMap] to read
 * @return A [Bundle] containing all data from the given [TagMap]
 */
fun TagMap.toBundle() = Bundle().also { write(TagStructureWriterBundle(it)) }
