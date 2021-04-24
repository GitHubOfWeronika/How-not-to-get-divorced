package com.example.how_not_to_get_divorced.ui.alarms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.how_not_to_get_divorced.R

class AlarmsFragment : Fragment() {

    private lateinit var alarmsModel: AlarmsModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        alarmsModel =
                ViewModelProvider(this).get(AlarmsModel::class.java)
        val root = inflater.inflate(R.layout.fragment_alarms, container, false)
        return root
    }
}