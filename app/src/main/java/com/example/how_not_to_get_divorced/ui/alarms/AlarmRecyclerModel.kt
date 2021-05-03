package com.example.how_not_to_get_divorced.ui.alarms

import androidx.lifecycle.LiveData
import com.example.how_not_to_get_divorced.model.Alarm
import com.example.how_not_to_get_divorced.model.Completion

class AlarmRecyclerModel {
    lateinit var alarm: Alarm
    lateinit var statistics : LiveData<Array<Map<Completion, Int>>>
}