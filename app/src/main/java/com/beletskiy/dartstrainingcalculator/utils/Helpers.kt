package com.beletskiy.dartstrainingcalculator.utils

import android.content.res.Resources

/// px to dp
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

/// dp to px
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
