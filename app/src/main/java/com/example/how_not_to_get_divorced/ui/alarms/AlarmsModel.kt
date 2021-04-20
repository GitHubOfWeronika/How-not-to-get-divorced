package com.example.how_not_to_get_divorced.ui.alarms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlarmsModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is reminders Fragment"
    }
    val text: LiveData<String> = _text
}