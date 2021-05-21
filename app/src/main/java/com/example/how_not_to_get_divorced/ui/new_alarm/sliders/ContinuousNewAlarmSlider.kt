package com.example.how_not_to_get_divorced.ui.new_alarm.sliders


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.model.AlarmRepetition
import com.google.android.material.slider.LabelFormatter
import kotlin.math.absoluteValue
import kotlin.math.log2
import kotlin.math.pow


class ContinuousNewAlarmSlider : NewAlarmSlider() {
    val H = 0.10001408194392808388906F
    val K = 0.03125F

    // Sets description of continuous slider
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        root.findViewById<TextView>(R.id.discrete_new_alarm_slider_desc).text =
            getString(R.string.adjust_continuous_slider)
        return root
    }

    // Returns current state AlarmRepetition object
    override fun getResult(): AlarmRepetition = AlarmRepetition.Continuous(
        monday = (f(weekdaySliders[0].value)),
        tuesday = (f(weekdaySliders[1].value)),
        wednesday = (f(weekdaySliders[2].value)),
        thursday = (f(weekdaySliders[3].value)),
        friday = (f(weekdaySliders[4].value)),
        saturday = (f(weekdaySliders[5].value)),
        sunday = (f(weekdaySliders[6].value)),
    )



    // Function changing scale to logarithmic
    fun f(x: Float): Float =
        ((2.0).pow(x * (H) - 5.0) - K).toFloat().absoluteValue

    fun fInv(y: Float) : Float = (log2(y+K) + 5F) / H

    override fun getFormatter(): LabelFormatter {
        return Formatter()
    }

    // Formatter for sliders label
    private inner class Formatter : LabelFormatter {
        override fun getFormattedValue(value: Float): String {
            val formatter = "%.2f " + getString(R.string.per_day)
            return formatter.format(f(value))
        }
    }

    override fun setResult(rep: AlarmRepetition) {
        collapse = true
        when (rep) {
            is AlarmRepetition.Continuous -> {
                weekdaySliders[0].value = fInv(rep.monday)
                weekdaySliders[1].value = fInv(rep.tuesday)
                weekdaySliders[2].value = fInv(rep.wednesday)
                weekdaySliders[3].value = fInv(rep.thursday)
                weekdaySliders[4].value = fInv(rep.friday)
                weekdaySliders[5].value = fInv(rep.saturday)
                weekdaySliders[6].value = fInv(rep.sunday)
                if (rep.monday == rep.tuesday && rep.tuesday == rep.wednesday &&
                    rep.wednesday == rep.thursday && rep.thursday == rep.friday) {
                    if (rep.saturday == 0F && rep.sunday == 0F) {
                        mainSlider.value = fInv(rep.monday)
                        weekendSwitch.isChecked = false
                    } else if (rep.saturday == rep.friday && rep.sunday == rep.friday){
                        mainSlider.value = fInv(rep.monday)
                        weekendSwitch.isChecked = true
                    }
                }
            }
            else -> {}
        }
    }
}