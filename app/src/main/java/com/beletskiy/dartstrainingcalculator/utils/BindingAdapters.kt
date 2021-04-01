package com.beletskiy.dartstrainingcalculator.utils

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.setPadding
import androidx.databinding.BindingAdapter
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.database.SavedToss

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
            Toss.Section.MISSED -> R.drawable.ic_wire_0
            Toss.Section.ONE -> R.drawable.ic_wire_1
            Toss.Section.TWO -> R.drawable.ic_wire_2
            Toss.Section.THREE -> R.drawable.ic_wire_3
            Toss.Section.FOUR -> R.drawable.ic_wire_4
            Toss.Section.FIVE -> R.drawable.ic_wire_5
            Toss.Section.SIX -> R.drawable.ic_wire_6
            Toss.Section.SEVEN -> R.drawable.ic_wire_7
            Toss.Section.EIGHT -> R.drawable.ic_wire_8
            Toss.Section.NINE -> R.drawable.ic_wire_9
            Toss.Section.TEN -> R.drawable.ic_wire_10
            Toss.Section.ELEVEN -> R.drawable.ic_wire_11
            Toss.Section.TWELVE -> R.drawable.ic_wire_12
            Toss.Section.THIRTEEN -> R.drawable.ic_wire_13
            Toss.Section.FOURTEEN -> R.drawable.ic_wire_14
            Toss.Section.FIFTEEN -> R.drawable.ic_wire_15
            Toss.Section.SIXTEEN -> R.drawable.ic_wire_16
            Toss.Section.SEVENTEEN -> R.drawable.ic_wire_17
            Toss.Section.EIGHTEEN -> R.drawable.ic_wire_18
            Toss.Section.NINETEEN -> R.drawable.ic_wire_19
            Toss.Section.TWENTY -> R.drawable.ic_wire_20
            Toss.Section.OUTER_BULLSEYE -> R.drawable.ic_wire_25
            Toss.Section.INNER_BULLSEYE -> R.drawable.ic_wire_b
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

// converts milliseconds (Long) to String (example "Friday, 25 Mar 2021, 18:13")
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
