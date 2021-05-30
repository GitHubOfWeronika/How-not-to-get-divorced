package com.example.how_not_to_get_divorced.ui.alarm_statistics

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.database.Mappers
import com.example.how_not_to_get_divorced.view.Histogram
import kotlin.math.ceil

class AlarmStatistics : Fragment() {
    private lateinit var statisticsModel: AlarmStatisticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.alarm_statistics_fragment, container, false)
        val arguments = arguments
        if (arguments != null){
            statisticsModel = AlarmStatisticsViewModel(requireContext(), arguments.getInt("alarmId"))
            val histTimeOfDay = root.findViewById<Histogram>(R.id.hist_time_of_day) // znajdź histogram
            statisticsModel.timeOfDay.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                histTimeOfDay.histogramData = it.map{x -> x.toFloat()}.toTypedArray()
                histTimeOfDay.displayRangeNumber = { x -> Mappers.intToTime(x.toInt()) }
            })
            val histTimeOfSuccess = root.findViewById<Histogram>(R.id.hist_time_of_success) // znajdź histogram
            statisticsModel.timeOfDaySuccess.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                histTimeOfSuccess.histogramData = it.map{x -> x.toFloat()}.toTypedArray()
                histTimeOfSuccess.displayRangeNumber = { x -> Mappers.intToTime(x.toInt()) }
            })
            val histTimeToCompletion = root.findViewById<Histogram>(R.id.hist_time_to_completion) // znajdź histogram
            statisticsModel.timeToCompletion.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                histTimeToCompletion.histogramData = it.map{x -> x.toFloat()}.toTypedArray()
                histTimeToCompletion.displayRangeNumber = { x -> x.toInt().toString()+" min" }
            })
            val histPerDay = root.findViewById<Histogram>(R.id.hist_alarms_per_day) // znajdź histogram
            statisticsModel.alarmsPerDay.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val max = if (it.maxOrNull() == null){
                    0
                } else {
                    it.maxOrNull()!!
                }
                histPerDay.setXAxis(max.toFloat()+0.5F, -0.5F)
                histPerDay.displayRangeNumber = {x -> ceil(x).toInt().toString()}
                histPerDay.displayRange = {d, _ -> ceil(d).toInt().toString()}
                histPerDay.histogramData = it.map{x -> x.toFloat()}.toTypedArray()

            })
            statisticsModel.continous.observe(viewLifecycleOwner, androidx.lifecycle.Observer { continuous ->
                if(!continuous){
                    histTimeOfDay.visibility = View.GONE
                    histTimeOfSuccess.visibility = View.GONE
                    root.findViewById<View>(R.id.text_time_of_day).visibility = View.GONE
                    root.findViewById<View>(R.id.text_time_of_success).visibility = View.GONE
                }
            })
        }
        return root
    }
}