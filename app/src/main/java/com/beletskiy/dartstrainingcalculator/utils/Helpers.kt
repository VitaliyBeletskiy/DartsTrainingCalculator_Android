package com.beletskiy.dartstrainingcalculator.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.text.format.DateFormat
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.database.SavedToss
import java.text.SimpleDateFormat

/// px to dp
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

/// dp to px
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/// returns position in the last series of 3
val Int.inSeriesOf3: Int
    get() = this - ((this - 1) / 3) * 3

/// takes the Long milliseconds and convert it to a string for display.
@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(milliseconds: Long, context: Context): String {
    val weekday = SimpleDateFormat("EEEE").format(milliseconds)
    val time = DateFormat.getTimeFormat(context).format(milliseconds)
    val date = DateFormat.getLongDateFormat(context).format(milliseconds)
    return "$weekday, $date, $time"
}

/// converts List<Toss> to String
fun convertSavedTossListToString(savedTossList: List<SavedToss>): String {
    val sb = StringBuilder()
    var numberInSeries = 0
    sb.apply {
        savedTossList.forEach {
            val toss = it.toToss()
            val sectionString = toss.section.value.toString()
            val ringString  = when(toss.ring) {
                Toss.Ring.X2, Toss.Ring.X3 -> "x${toss.ring.value}"
                Toss.Ring.X1 -> ""
            }
            append(sectionString, ringString)
            repeat(6 - (sectionString.length + ringString.length)) {
                append(" ")
            }

            numberInSeries ++
            if (numberInSeries == 3) {
                appendLine()
                numberInSeries = 0
            }
        }
    }
    return sb.toString()
}