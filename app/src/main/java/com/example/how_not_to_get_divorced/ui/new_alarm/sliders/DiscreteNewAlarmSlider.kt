package com.example.how_not_to_get_divorced.ui.new_alarm.sliders

import com.example.how_not_to_get_divorced.model.AlarmRepetition
import com.google.android.material.slider.LabelFormatter


class DiscreteNewAlarmSlider(collapse: Boolean, changeCollapse: (Boolean) -> Unit)
    : NewAlarmSlider(collapse, changeCollapse) {

    // Returns current state AlarmRepetition object
    override fun getResult() : AlarmRepetition = AlarmRepetition.Discrete (
        monday = (weekdaySliders[0].value / 100.0F),
        tuesday= (weekdaySliders[1].value / 100.0F),
        wednesday= (weekdaySliders[2].value / 100.0F),
        thursday= (weekdaySliders[3].value / 100.0F),
        friday= (weekdaySliders[4].value / 100.0F),
        saturday= (weekdaySliders[5].value / 100.0F),
        sunday= (weekdaySliders[6].value / 100.0F),
    )

    override fun getFormatter(): LabelFormatter {
        return Formatter()
    }

    // Formatter for sliders label (adding %)
    private inner class Formatter : LabelFormatter{
        override fun getFormattedValue(value: Float) = "%.1f %%".format(value)
    }
}