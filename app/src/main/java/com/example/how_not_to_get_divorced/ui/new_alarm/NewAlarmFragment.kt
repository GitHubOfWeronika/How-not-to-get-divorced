package com.example.how_not_to_get_divorced.ui.new_alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.how_not_to_get_divorced.R

class NewAlarmFragment : Fragment() {

    private lateinit var newAlarmModel: NewAlarmModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        newAlarmModel =
                ViewModelProvider(this).get(NewAlarmModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_alarm, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        newAlarmModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}