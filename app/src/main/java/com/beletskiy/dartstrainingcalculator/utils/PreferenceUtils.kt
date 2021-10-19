package com.beletskiy.dartstrainingcalculator.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.beletskiy.dartstrainingcalculator.R

/** Utility class to retrieve shared preferences. */
object PreferenceUtils {

    /** Reads 'Start Points' from Shared Preferences. If it doesn't exist returns 501 as default.  */
    fun getStartPoints(context: Context): Int {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val gameString = sharedPreferences.getString(
            context.getString(R.string.game_key),
            context.getString(R.string.default_start_points)
        )
        return gameString?.toInt() ?: DEFAULT_START_POINTS
    }
}
