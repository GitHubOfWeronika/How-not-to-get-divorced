package com.example.how_not_to_get_divorced.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.how_not_to_get_divorced.R

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
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        statisticsModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}