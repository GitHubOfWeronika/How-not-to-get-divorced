package com.example.how_not_to_get_divorced.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.how_not_to_get_divorced.R
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
        val histogram = root.findViewById<Histogram>(R.id.histogram) // znajdź histogram
        val data : Array<Float> = Array(2000) {(sqrt(-2.0 * ln(random())) * cos(2.0 * PI * random())).toFloat() } // wygeneruj dane o rozkładzie normalnym
        histogram.histogramData = data // przekaż dane do histogramu
        histogram.f = { // podaj oczekiwany rozkłąd doanych
            (1.0/sqrt(2*PI)* exp(-it*it/2.0)).toFloat()
        }
        return root
    }
}