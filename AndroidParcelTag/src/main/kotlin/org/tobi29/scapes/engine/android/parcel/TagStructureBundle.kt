package org.tobi29.scapes.engine.android.parcel

import android.os.Bundle
import org.tobi29.scapes.engine.utils.tag.TagMap
import org.tobi29.scapes.engine.utils.tag.write

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
