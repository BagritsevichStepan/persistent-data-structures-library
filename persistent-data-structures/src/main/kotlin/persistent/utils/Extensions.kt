package persistent.utils

import kotlin.math.ln

fun Boolean.toULong(): ULong = if (this) 1u else 0u

fun Int.logWithBase(base: Double): Int = (ln(this.toDouble()) / ln(base)).toInt()