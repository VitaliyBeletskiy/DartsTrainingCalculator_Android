package com.beletskiy.dartstrainingcalculator.utils

import android.widget.ImageButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.setPadding
import androidx.databinding.BindingAdapter
import com.beletskiy.dartstrainingcalculator.R

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