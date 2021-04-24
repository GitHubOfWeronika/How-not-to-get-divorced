package com.example.how_not_to_get_divorced.ui.new_alarm.sliders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.model.AlarmRepetition
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial

/**
 * Abstract class of slider fragment
 *
 * collapse - if has simple layout (no weekdays)
 * changeCollapse - function of setting collapse parameter
 */
abstract class NewAlarmSlider(private var collapse: Boolean, val changeCollapse: (Boolean) -> Unit) : Fragment() {
    lateinit var root: View
    lateinit var weekdaySliders:Array<Slider> // array of weekdays slider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_descrete_new_alarm, container, false)
        // handle toggle weekday view
        root.findViewById<Button>(R.id.discrete_new_alarm_collapse_btn).setOnClickListener {
            changeCollapse(collapse)
        }

        weekdaySliders = arrayOf(
            root.findViewById(R.id.discrete_new_alarm_slider_mon),
            root.findViewById(R.id.discrete_new_alarm_slider_tue),
            root.findViewById(R.id.discrete_new_alarm_slider_wed),
            root.findViewById(R.id.discrete_new_alarm_slider_thu),
            root.findViewById(R.id.discrete_new_alarm_slider_fri),
            root.findViewById(R.id.discrete_new_alarm_slider_sat),
            root.findViewById(R.id.discrete_new_alarm_slider_sun),
        )
        // sets formatter of each slider
        for (slider in weekdaySliders) {
            slider.setLabelFormatter(getFormatter())
        }
        val mainSlider = root.findViewById<Slider>(R.id.discrete_new_alarm_slider)
        val weekendSwitch = root.findViewById<SwitchMaterial>(R.id.discrete_new_alarm_weekend)
        // sets weekday sliders to match "include weekends" switch
        weekendSwitch.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                for (slider in weekdaySliders.drop(5)) {
                    slider.value = mainSlider.value
                }
            } else {
                for (slider in weekdaySliders.drop(5)) {
                    slider.value = 0.0F
                }
            }
        }
        // add formatter to main single slider
        mainSlider.setLabelFormatter(getFormatter())
        // sets weekday sliders to match main single slider
        mainSlider.addOnChangeListener { _, value, fromUser ->
            if(fromUser){
                if (weekendSwitch.isChecked) {
                    for (slider in weekdaySliders) {
                        slider.value = value
                    }
                } else {
                    for (slider in weekdaySliders.take(5)) {
                        slider.value = value
                    }
                    for (slider in weekdaySliders.drop(5)) {
                        slider.value = 0.0F
                    }
                }
            }
        }
        setCollapse(!collapse)
        return root
    }

    fun setCollapse(visible: Boolean){
        collapse = !visible
        if (! this::root.isInitialized) return // if root was not initialized
        val simpl = root.findViewById<LinearLayout>(R.id.discrete_new_alarm_simple_slider) // simple layout
        val compl = root.findViewById<LinearLayout>(R.id.discrete_new_alarm_complex_slider) // complex (weekday) layout
        val btn = root.findViewById<Button>(R.id.discrete_new_alarm_collapse_btn)
        if(visible){
            btn.text = getString(R.string.hide_weekdays)
            simpl.visibility = View.GONE
            compl.visibility = View.VISIBLE
        } else {
            btn.text = getString(R.string.show_weekdays)
            compl.visibility = View.GONE
            simpl.visibility = View.VISIBLE
        }
    }
    abstract fun getResult() : AlarmRepetition
    abstract fun getFormatter() : LabelFormatter
}