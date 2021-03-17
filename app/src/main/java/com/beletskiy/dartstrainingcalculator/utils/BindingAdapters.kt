package com.beletskiy.dartstrainingcalculator.utils

import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.setPadding
import androidx.databinding.BindingAdapter
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.data.Toss

@BindingAdapter("android:background")
fun ImageButton.setBackground(value: Boolean?) {
    value?.let {
        val bgSelected = AppCompatResources.getDrawable(context, R.drawable.toss_tile_background_selected);
        val bg = AppCompatResources.getDrawable(context, R.drawable.toss_tile_background);
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
        val imageId = when(section) {
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