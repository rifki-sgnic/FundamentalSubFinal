package com.dicoding.picodiploma.fundamentalsubfinal.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.picodiploma.fundamentalsubfinal.R
import com.dicoding.picodiploma.fundamentalsubfinal.receiver.AlarmReceiver

class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var REMIND: String

    private lateinit var switchPreference: SwitchPreference

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
    }

    private fun init() {
        REMIND = resources.getString(R.string.key_switch)

        switchPreference = findPreference<SwitchPreference>(REMIND) as SwitchPreference
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        switchPreference.isChecked = sh.getBoolean(REMIND, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == REMIND) {
            val alarmManager = AlarmReceiver()
            if (switchPreference.isChecked) {
                alarmManager.setRepeatingAlarm(requireActivity(), AlarmReceiver.EXTRA_MESSAGE)
                Toast.makeText(
                    context,
                    getString(R.string.daily_reminder_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                alarmManager.cancelAlarm(requireActivity())
                Toast.makeText(
                    context,
                    getString(R.string.daily_reminder_disabled),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}