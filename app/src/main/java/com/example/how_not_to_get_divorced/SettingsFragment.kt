package com.example.how_not_to_get_divorced

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.how_not_to_get_divorced.view.TimePreferenceDialog
import com.example.how_not_to_get_divorced.view.TimeSelectPreference

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        if(preference is TimeSelectPreference) {
            val dialog = TimePreferenceDialog.newInstance(preference.key)
            dialog.setTargetFragment(this, 0)
            dialog.show(parentFragmentManager, "TimeSelector")
        }
        else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}