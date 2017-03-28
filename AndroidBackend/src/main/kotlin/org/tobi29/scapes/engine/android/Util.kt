package org.tobi29.scapes.engine.android

import android.content.Context
import org.tobi29.scapes.engine.utils.io.filesystem.path

val Context.filesPath get() = path(filesDir.toString())

val Context.cachePath get() = path(cacheDir.toString())
