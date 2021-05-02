package com.example.how_not_to_get_divorced.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.Room
import com.example.how_not_to_get_divorced.database.entities.*
import com.example.how_not_to_get_divorced.model.Alarm
import com.example.how_not_to_get_divorced.model.AlarmRepetition
import com.example.how_not_to_get_divorced.model.Completion
import com.example.how_not_to_get_divorced.model.Task
import java.lang.Exception
import java.util.*

/**
 * Class to access application database
 */
class DBAccess(context: Context){
    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-name"
    )
        .fallbackToDestructiveMigration()
        .build()
        .getDao()

    /**
     * Get LiveData of current statistics of selected statistics from <until> to current date
     * Data structure:
     * [
     *  {Completion.DONE => <tasks done today>, Completion.FAILED => <tasks failed today>, Completion.WAITING => <tasks waiting today>},
     *  {Completion.DONE => <tasks done yesterday>, Completion.FAILED => <tasks failed yesterday>, Completion.WAITING => <tasks waiting yesterday>},
     *  ....
     * ]
     */
    fun getStatistics(alarm: Alarm, until: Date): LiveData<Array<Map<Completion, Int>>> {
        val id = alarm.id
        if (id != null) {
            return db.getStatistics(id, until.time / 1000L).map {
                Mappers.statisticsToArray(it, until)
            }
        }
        throw Exception("No id in alarm")
    }

    /**
     * List of all not deleted alarms
     */
    fun getAllAlarms() = db.getAllAlarms()

    fun insertAlarm(alarm: Alarm) {
        setAlarm(alarm, 0)
    }

    fun updateAlarm(alarm: Alarm) {
        var id = 0
        val currentId = alarm.id
        if (currentId != null){
            id = currentId
            val continuous = db.getContinuous(id)
            if(continuous != null){
                db.deleteContinuous(continuous)
            }
            val discrete = db.getDiscrete(id)
            if(discrete != null){
                db.deleteDiscrete(discrete)
            }
        }
        setAlarm(alarm, id)
    }

    private fun setAlarm(alarm: Alarm, id: Int){
        val oldRepetition = alarm.repetition;
        when (oldRepetition){
            is AlarmRepetition.Discrete -> {
                val alarmEntity = AlarmEntity(
                    id,
                    alarm.name,
                    alarm.category.id,
                    alarm.active,
                    alarm.deleted,
                    alarm.created.time / 1000L
                )
                val newId = if (id == 0){
                    val rowId = db.insertAlarm(alarmEntity)
                    val insertedAlarm = db.getAlarmByRowId(rowId)
                    insertedAlarm.id
                } else {
                    db.updateAlarm(alarmEntity)
                    id
                }
                val repetition = DiscreteEntity(
                    newId,
                    oldRepetition.monday,
                    oldRepetition.tuesday,
                    oldRepetition.wednesday,
                    oldRepetition.thursday,
                    oldRepetition.friday,
                    oldRepetition.saturday,
                    oldRepetition.sunday
                )
                db.insertDiscrete(repetition)
            }
            is AlarmRepetition.Continuous -> {
                val alarmEntity = AlarmEntity(
                    id,
                    alarm.name,
                    alarm.category.id,
                    alarm.active,
                    alarm.deleted,
                    alarm.created.time / 1000L
                )
                val newId = if (id == 0){
                    val rowId = db.insertAlarm(alarmEntity)
                    val insertedAlarm = db.getAlarmByRowId(rowId)
                    insertedAlarm.id
                } else {
                    db.updateAlarm(alarmEntity)
                    id
                }
                val repetition = ContinuousEntity(
                    newId,
                    oldRepetition.monday,
                    oldRepetition.tuesday,
                    oldRepetition.wednesday,
                    oldRepetition.thursday,
                    oldRepetition.friday,
                    oldRepetition.saturday,
                    oldRepetition.sunday
                )
                db.insertContinuous(repetition)
            }
        }
    }

    /**
     * List of all tasks for selected date
     */
    fun getAllTaskForADay(date: Date) = db.getAllTasksForDay(date)

    fun updateTest(task: Task){
        if (task.id != null && task.alarm.id != null) {
            db.updateTask(
                TaskEntity(
                    task.id!!,
                    task.alarm.id!!,
                    task.date.time,
                    task.completion.id
                )
            )
        }
    }

    fun insertTask(task: Task){
        if (task.alarm.id != null) {
            db.insertTask(
                TaskEntity(
                    0,
                    task.alarm.id!!,
                    task.date.time,
                    task.completion.id
                )
            )
        }
    }

}