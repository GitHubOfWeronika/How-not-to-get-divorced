package com.example.how_not_to_get_divorced.ui.new_alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewAlarmModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is new reminders Fragment"
    }
    val text: LiveData<String> = _text
}