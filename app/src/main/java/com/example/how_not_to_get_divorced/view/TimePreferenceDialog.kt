package com.example.how_not_to_get_divorced.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.preference.PreferenceDialogFragmentCompat

class TimePreferenceDialog : PreferenceDialogFragmentCompat() {

    lateinit var timepicker: TimePicker

    override fun onCreateDialogView(context: Context?): View {
        timepicker = TimePicker(context)
        return timepicker
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)

        val value = (preference as TimeSelectPreference)
            .getCurrentValue()
        timepicker.setIs24HourView(true)
        timepicker.hour = value / 3600
        timepicker.minute = (value / 60) % 60
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if(positiveResult) {
            val minutesAfterMidnight = ((timepicker.hour * 60) + timepicker.minute) * 60
            (preference as TimeSelectPreference).saveValue(minutesAfterMidnight)
            preference.summary = TimeSelectPreference.formatSecondsOfDay(minutesAfterMidnight)

        }
    }

    companion object {
        fun newInstance(key: String): TimePreferenceDialog {
            val fragment = TimePreferenceDialog()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle

            return fragment
        }
    }
}