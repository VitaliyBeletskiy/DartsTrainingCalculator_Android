package com.beletskiy.dartstrainingcalculator.fragments.settings

import android.os.Bundle
import android.view.WindowManager
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.beletskiy.dartstrainingcalculator.MainActivity
import com.beletskiy.dartstrainingcalculator.R

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // to show App Version in Settings
        val appVersion = requireContext().packageManager.getPackageInfo(
            requireContext().packageName,
            0
        ).versionName
        val summaryProvider = Preference.SummaryProvider<Preference> {
            "Version $appVersion"
        }
        findPreference<Preference>("version")?.summaryProvider = summaryProvider

        // set PreferenceChange listeners
        val preventSleepPreference: Preference? =
            preferenceManager.findPreference(getString(R.string.prevent_sleep_key))
        preventSleepPreference?.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        // switch ON/OFF "prevent phone from sleeping"
        preference?.let {
            if (it.key == getString(R.string.prevent_sleep_key)) {
                if (newValue == true) {
                    (requireNotNull(this.activity) as MainActivity).window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    (requireNotNull(this.activity) as MainActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }
        }
        return true
    }
}