package com.beletskiy.dartstrainingcalculator.fragments.settings

import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.utils.TAG


class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val appVersion = requireContext().packageManager.getPackageInfo(
            requireContext().packageName,
            0
        ).versionName
        val summaryProvider = Preference.SummaryProvider<Preference> {
            "Version $appVersion"
        }
        findPreference<Preference>("version")?.summaryProvider = summaryProvider

        // set Preference Change Listeners
        val preventSleepPreference: Preference? =
            preferenceManager.findPreference(getString(R.string.prevent_sleep_key))
        preventSleepPreference?.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        preference?.let {
            if (preference.key == getString(R.string.prevent_sleep_key)) {
                Log.i(
                    TAG,
                    "onPreferenceChange: preventSleepPreference CHANGED, new value = $newValue"
                )
                // TODO: 22/03/2021 handle "prevent phone from sleeping"
            }
        }
        return true
    }
}