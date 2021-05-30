package com.example.how_not_to_get_divorced.ui.statistics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.how_not_to_get_divorced.R
import com.example.how_not_to_get_divorced.database.Mappers
import com.example.how_not_to_get_divorced.view.Histogram
import java.lang.Math.random
import kotlin.math.*

class StatisticsFragment : Fragment() {

    private lateinit var statisticsModel: StatisticsModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        statisticsModel =
                ViewModelProvider(this).get(StatisticsModel::class.java)
        val root = inflater.inflate(R.layout.fragment_statistics, container, false)
        val histTimeOfDay = root.findViewById<Histogram>(R.id.hist_time_of_day) // znajdź histogram
        statisticsModel.timeOfDay.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("stat_test", it.size.toString())
            histTimeOfDay.histogramData = it.map{x -> x.toFloat()}.toTypedArray()
            histTimeOfDay.displayRangeNumber = { x -> Mappers.intToTime(x.toInt()) }
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
        return root
    }
}