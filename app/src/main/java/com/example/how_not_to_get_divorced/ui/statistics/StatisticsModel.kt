package com.example.how_not_to_get_divorced.ui.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.how_not_to_get_divorced.database.DBAccess
import com.example.how_not_to_get_divorced.model.Completion

class StatisticsModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is statistics Fragment"
    }
    private val _alarmsPerDay = MediatorLiveData<List<Int>>().apply {
        value = listOf()
    }
    val db = DBAccess(application.applicationContext)
    val text: LiveData<String> = _text
    val alarmsPerDay: LiveData<List<Int>> = _alarmsPerDay
    val timeOfDay = db.getTimeOfTasks(null, listOf(Completion.DONE.id, Completion.FAILED.id, Completion.WAITING.id))
    val timeToCompletion = db.getTimeToFinish(null)

    init {
        _alarmsPerDay.apply {
            addSource(db.getFirstAlarmDate()) { firstDate ->
                addSource(db.getAllStatistics(firstDate)) { x ->
                    value =
                        x.map { y -> y[Completion.DONE]!! + y[Completion.FAILED]!! + y[Completion.WAITING]!! }
                }
            }
        }
    }
}