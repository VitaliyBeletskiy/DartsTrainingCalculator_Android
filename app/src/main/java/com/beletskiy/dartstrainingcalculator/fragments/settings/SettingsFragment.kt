package com.beletskiy.dartstrainingcalculator.fragments.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.preference.ListPreference
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
            //"Version $appVersion"
            getString(R.string.version, appVersion)
        }
        findPreference<Preference>("version")?.summaryProvider = summaryProvider

        // set PreferenceChange listeners
        preferenceManager.findPreference<Preference>(getString(R.string.prevent_sleep_key))
            ?.onPreferenceChangeListener = this
        preferenceManager.findPreference<ListPreference>(getString(R.string.game_key))
            ?.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {

        if (preference == null) return false

        when (preference.key) {
            // switch ON/OFF "prevent phone from sleeping"
            getString(R.string.prevent_sleep_key) -> {
                if (newValue == true) {
                    (requireNotNull(this.activity) as MainActivity).window.addFlags(
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    )
                } else {
                    (requireNotNull(this.activity) as MainActivity).window.clearFlags(
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    )
                }
            }
            // "Choose game" ListPreference
            getString(R.string.game_key) -> {
                val oldValue = (preference as ListPreference).value
                if (oldValue != newValue) {
                    AlertDialog.Builder(context).run {
                        setTitle(getString(R.string.change_game_title))
                        setMessage(getString(R.string.change_game_message))
                        setPositiveButton(getString(R.string.confirm), null)
                        setNegativeButton(getString(R.string.cancel)) { _, _ ->
                            preference.value = oldValue
                        }
                        setCancelable(false)
                        create()
                        show()
                    }
                }
            }
            else -> {
            }
        }

        return true
    }
}