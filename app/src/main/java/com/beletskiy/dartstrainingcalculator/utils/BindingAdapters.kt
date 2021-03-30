package com.beletskiy.dartstrainingcalculator.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.setPadding
import androidx.databinding.BindingAdapter
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.database.SavedToss
import java.text.SimpleDateFormat

@BindingAdapter("android:background")
fun ImageButton.setBackground(value: Boolean?) {
    value?.let {
        val bgSelected =
            AppCompatResources.getDrawable(context, R.drawable.toss_tile_background_selected)
        val bg = AppCompatResources.getDrawable(context, R.drawable.toss_tile_background)
        if (it) {
            this.background = bgSelected
        } else {
            this.background = bg
        }
        // as setting background removes padding
        // FIXME: don't like hardcoded value here
        this.setPadding(20.px)
    }
}

@BindingAdapter("numberImage")
fun bindNumberImage(imageView: ImageView, section: Toss.Section?) {
    section?.let {
        val imageId = when (section) {
            Toss.Section.MISSED -> R.drawable.ic_0
            Toss.Section.ONE -> R.drawable.ic_1
            Toss.Section.TWO -> R.drawable.ic_2
            Toss.Section.THREE -> R.drawable.ic_3
            Toss.Section.FOUR -> R.drawable.ic_4
            Toss.Section.FIVE -> R.drawable.ic_5
            Toss.Section.SIX -> R.drawable.ic_6
            Toss.Section.SEVEN -> R.drawable.ic_7
            Toss.Section.EIGHT -> R.drawable.ic_8
            Toss.Section.NINE -> R.drawable.ic_9
            Toss.Section.TEN -> R.drawable.ic_10
            Toss.Section.ELEVEN -> R.drawable.ic_11
            Toss.Section.TWELVE -> R.drawable.ic_12
            Toss.Section.THIRTEEN -> R.drawable.ic_13
            Toss.Section.FOURTEEN -> R.drawable.ic_14
            Toss.Section.FIFTEEN -> R.drawable.ic_15
            Toss.Section.SIXTEEN -> R.drawable.ic_16
            Toss.Section.SEVENTEEN -> R.drawable.ic_17
            Toss.Section.EIGHTEEN -> R.drawable.ic_18
            Toss.Section.NINETEEN -> R.drawable.ic_19
            Toss.Section.TWENTY -> R.drawable.ic_20
            Toss.Section.OUTER_BULLSEYE -> R.drawable.ic_ob
            Toss.Section.INNER_BULLSEYE -> R.drawable.ic_ib
        }
        imageView.setImageResource(imageId)
    }
}

@BindingAdapter("ringImage")
fun bindNumberImage(imageView: ImageView, ring: Toss.Ring?) {
    ring?.let {
        val imageId = when (ring) {
            Toss.Ring.X1 -> R.drawable.ic_badge_x1
            Toss.Ring.X2 -> R.drawable.ic_badge_x2
            Toss.Ring.X3 -> R.drawable.ic_badge_x3
        }
        imageView.setImageResource(imageId)
    }
}

// converts milliseconds (Long) to "Friday, 25 Mar 2021, 18:13"
@BindingAdapter("timestamp")
fun TextView.fromMillisecondsToDate(timestamp: Long?) {
    timestamp?.let {
        text = convertLongToDateString(it, context)
    }
}

@BindingAdapter("savedTossList")
fun TextView.fromSavedTossListToString(savedTossList: List<SavedToss>?) {
    savedTossList?.let {
        text = convertSavedTossListToString(it)
    }
}

// TODO: убрать отсюда?
/// takes the Long milliseconds and convert it to a string for display.
@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(milliseconds: Long, context: Context): String {
    val weekday = SimpleDateFormat("EEEE").format(milliseconds)
    val time = DateFormat.getTimeFormat(context).format(milliseconds)
    val date = DateFormat.getLongDateFormat(context).format(milliseconds)
    return "$weekday, $date, $time"
}

// TODO: shitcode, for testing only
/// converts List<Toss> to a string
fun convertSavedTossListToString(savedTossList: List<SavedToss>): String {
    val sb = StringBuilder()
    var numberInSeries = 0
    sb.apply {
        savedTossList.forEach {
            append("${Toss.Section.values()[it.section].value}")
            when(val ring = Toss.Ring.values()[it.ring]) {
                Toss.Ring.X2, Toss.Ring.X3 -> append(" (x${ring.value}), ")
                Toss.Ring.X1 -> append(", ")
            }
            numberInSeries ++
            if (numberInSeries == 3) {
                append("\n")
                numberInSeries = 0
            }
        }
    }
    return sb.toString()
}