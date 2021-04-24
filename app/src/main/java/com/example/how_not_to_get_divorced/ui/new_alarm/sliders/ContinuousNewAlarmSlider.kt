package com.example.how_not_to_get_divorced.ui.new_alarm.sliders


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.model.AlarmRepetition
import com.google.android.material.slider.LabelFormatter
import org.w3c.dom.Text
import kotlin.math.absoluteValue
import kotlin.math.pow


class ContinuousNewAlarmSlider(collapse: Boolean, changeCollapse: (Boolean) -> Unit) :
    NewAlarmSlider(collapse, changeCollapse) {

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
        ((2.0).pow(x / 10.0 - 5.0) + 0.00031258 * (x - 100.0)).toFloat().absoluteValue

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
}