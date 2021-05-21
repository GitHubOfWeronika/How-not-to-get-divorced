package com.example.how_not_to_get_divorced.ui.new_alarm.sliders

import com.example.how_not_to_get_divorced.model.AlarmRepetition
import com.google.android.material.slider.LabelFormatter


class DiscreteNewAlarmSlider : NewAlarmSlider() {

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

    override fun setResult(rep: AlarmRepetition) {
        collapse = true
        when (rep) {
            is AlarmRepetition.Discrete -> {
                weekdaySliders[0].value = rep.monday * 100F
                weekdaySliders[1].value = rep.tuesday * 100F
                weekdaySliders[2].value = rep.wednesday * 100F
                weekdaySliders[3].value = rep.thursday * 100F
                weekdaySliders[4].value = rep.friday * 100F
                weekdaySliders[5].value = rep.saturday * 100F
                weekdaySliders[6].value = rep.sunday * 100F
                if (rep.monday == rep.tuesday && rep.tuesday == rep.wednesday &&
                    rep.wednesday == rep.thursday && rep.thursday == rep.friday) {
                    if (rep.saturday == 0F && rep.sunday == 0F) {
                        mainSlider.value = rep.monday * 100F
                        weekendSwitch.isChecked = false
                    } else if (rep.saturday == rep.friday && rep.sunday == rep.friday){
                        mainSlider.value = rep.monday * 100F
                        weekendSwitch.isChecked = true
                    }
                }
            }
            else -> {}
        }
    }
}