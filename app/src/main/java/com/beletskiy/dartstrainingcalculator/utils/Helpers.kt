package com.beletskiy.dartstrainingcalculator.utils

import android.content.res.Resources

/// px to dp
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

/// dp to px
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/// returns what position in the last series of 3
val Int.inSeriesOf3: Int
    get() = this - ((this - 1) / 3) * 3