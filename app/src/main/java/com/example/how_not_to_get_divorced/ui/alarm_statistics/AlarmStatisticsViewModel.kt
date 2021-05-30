package com.example.how_not_to_get_divorced.ui.alarm_statistics

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.map
import com.example.how_not_to_get_divorced.database.DBAccess
import com.example.how_not_to_get_divorced.model.AlarmRepetition
import com.example.how_not_to_get_divorced.model.Completion

class AlarmStatisticsViewModel(ctx: Context, alarm: Int) {
    private val _statistics = MediatorLiveData<Array<Map<Completion, Int>>>().apply {
        value = arrayOf()
    }
    private val _alarmsPerDay = MediatorLiveData<List<Int>>().apply {
        value = listOf()
    }

    val db = DBAccess(ctx)
    val name = db.getAlarmById(alarm).map{a -> a.name}
    val continous = db.getAlarmById(alarm).map{a -> when(a.repetition){
        is AlarmRepetition.Continuous -> true
        is AlarmRepetition.Discrete -> false
    } }
    val statistics : LiveData<Array<Map<Completion, Int>>> = _statistics
    val alarmsPerDay : LiveData<List<Int>> = _alarmsPerDay
    val timeOfDay = db.getTimeOfTasks(alarm, listOf(Completion.DONE.id, Completion.FAILED.id, Completion.WAITING.id))
    val timeOfDaySuccess = db.getTimeOfTasks(alarm, listOf(Completion.DONE.id))
    val timeToCompletion = db.getTimeToFinish(alarm)

    init{
        _statistics.apply {
            addSource(db.getAlarmById(alarm)) { alarm ->
                addSource(db.getStatistics(alarm, alarm.created)) { x ->
                    value = x
                }
            }
        }

        _alarmsPerDay.apply {
            addSource(db.getAlarmById(alarm)) { alarm ->
                addSource(db.getStatistics(alarm, alarm.created)) { x ->
                    value = x.map { y -> y[Completion.DONE]!! + y[Completion.FAILED]!! + y[Completion.WAITING]!! }
                }
            }
        }
    }
}