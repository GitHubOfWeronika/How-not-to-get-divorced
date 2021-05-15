package com.example.how_not_to_get_divorced.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.*
import androidx.room.Dao
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.how_not_to_get_divorced.database.entities.*
import com.example.how_not_to_get_divorced.model.Alarm
import com.example.how_not_to_get_divorced.model.Task
import java.text.SimpleDateFormat
import java.util.*

@Dao
interface  Dao {
    @RawQuery(
        observedEntities = [AlarmEntity::class, ContinuousEntity::class, DiscreteEntity::class]
    )
    fun getAllAlarmsQuery(query: SupportSQLiteQuery) : LiveData<List<FullAlarm>>

    @RawQuery
    fun getAllAlarmsQueryRes(query: SupportSQLiteQuery) : List<FullAlarm>

    fun getContinuousAlarmsForNotification() : List<Alarm> {
        val query = "SELECT alarm.*, monday, tuesday, wednesday, thursday, friday, saturday, sunday, 1 AS type " +
                    "FROM alarm INNER JOIN continuous ON alarm.id = continuous.id WHERE deleted = False AND active = True"
        val simpleSQLiteQuery = SimpleSQLiteQuery(query, arrayOf())
        return getAllAlarmsQueryRes(simpleSQLiteQuery).map {x -> Mappers.toAlarm(x)}
    }

    fun getDiscreteAlarmsForNotification() : List<Alarm> {
        val query = "SELECT alarm.*, monday, tuesday, wednesday, thursday, friday, saturday, sunday, 0 AS type " +
                "FROM alarm INNER JOIN discrete ON alarm.id = discrete.id WHERE deleted = False AND active = True"
        val simpleSQLiteQuery = SimpleSQLiteQuery(query, arrayOf())
        return getAllAlarmsQueryRes(simpleSQLiteQuery).map {x -> Mappers.toAlarm(x)}
    }

    fun getAllAlarms() : LiveData<List<Alarm>>{
        val query =
            "SELECT alarm.*, monday, tuesday, wednesday, thursday, friday, saturday, sunday, 0 AS type " +
            "FROM alarm INNER JOIN discrete ON alarm.id = discrete.id WHERE deleted = False UNION ALL " +
            "SELECT alarm.*, monday, tuesday, wednesday, thursday, friday, saturday, sunday, 1 AS type " +
            "FROM alarm INNER JOIN continuous ON alarm.id = continuous.id WHERE deleted = False ORDER BY created"
        val simpleSQLiteQuery = SimpleSQLiteQuery(query, arrayOf())
        return getAllAlarmsQuery(simpleSQLiteQuery).map {x -> x.map {y -> Mappers.toAlarm(y)}}
    }

    @RawQuery(
        observedEntities = [AlarmEntity::class, ContinuousEntity::class, DiscreteEntity::class, TaskEntity::class]
    )
    fun getAllTasksForDayQuery(query: SupportSQLiteQuery) : LiveData<List<FullTask>>

    fun getAllTasksForDay(date: Date) : LiveData<List<Task>>{
        val query =
            "SELECT task.*, name, category, active, deleted, created, monday, tuesday, wednesday, thursday, friday, saturday, sunday, 0 AS type " +
            "FROM task INNER JOIN alarm ON task.alarm = alarm.id INNER JOIN discrete ON alarm.id = discrete.id " +
            "WHERE SUBSTR(DATETIME(date, 'unixepoch', 'localtime'), 1, 10) = :date UNION ALL " +
            "SELECT task.*, name, category, active, deleted, created, monday, tuesday, wednesday, thursday, friday, saturday, sunday, 1 AS type " +
            "FROM task INNER JOIN alarm ON task.alarm = alarm.id INNER JOIN continuous ON alarm.id = continuous.id " +
            "WHERE SUBSTR(DATETIME(date, 'unixepoch', 'localtime'), 1, 10) = :date ORDER BY date"
        val formatted = SimpleDateFormat("yyyy-MM-dd").format(date)
        Log.d("my_db", formatted)
        val simpleSQLiteQuery = SimpleSQLiteQuery(query, arrayOf(formatted))
        return getAllTasksForDayQuery(simpleSQLiteQuery).map {x -> x.map {y -> Mappers.toTask(y)}}
    }

    @RawQuery(
        observedEntities = [TaskEntity::class]
    )
    fun getStatisticsQuery(query: SupportSQLiteQuery) : LiveData<List<TasksStatistics>>

    fun getStatistics(alarmId: Int, until: Long) : LiveData<List<TasksStatistics>>{
        val query =
            "SELECT SUBSTR(DATETIME(task.date, 'unixepoch', 'localtime'), 1, 10) AS date, completion " +
            "FROM task " +
            "WHERE alarm = :id AND task.date >= :until "
        val simpleSQLiteQuery = SimpleSQLiteQuery(query, arrayOf(alarmId, until))
        return getStatisticsQuery(simpleSQLiteQuery)
    }

    @RawQuery(
        observedEntities = [TaskEntity::class]
    )
    fun getTimeStatisticsQuery(query: SupportSQLiteQuery) : LiveData<List<Long>>

    fun getTimeStatistics(alarmId: Int, until: Long, completion: Int) : LiveData<List<Long>>{
        val query =
            "SELECT changed - date FROM task " +
            "WHERE alarm = :id AND task.date >= :until AND changed IS NOT NULL AND completion = :completion"
        val simpleSQLiteQuery = SimpleSQLiteQuery(query, arrayOf(alarmId, until, completion))
        return getTimeStatisticsQuery(simpleSQLiteQuery)
    }

    @Update
    fun updateTask(task: TaskEntity)

    @Delete
    fun deleteTask(task: TaskEntity)

    @Insert
    fun insertTask(task: TaskEntity) : Long

    @Update
    fun updateAlarm(alarm: AlarmEntity)

    @Delete
    fun deleteAlarm(alarm: AlarmEntity)

    @Query("SELECT * FROM alarm WHERE rowid = :rowId")
    fun getAlarmByRowId(rowId: Long) : AlarmEntity

    @Insert
    fun insertAlarm(alarm: AlarmEntity) : Long

    @Update
    fun updateContinuous(continuous: ContinuousEntity)

    @Delete
    fun deleteContinuous(continuous: ContinuousEntity)

    @Query("SELECT * FROM continuous WHERE id = :id")
    fun getContinuous(id: Int): ContinuousEntity?

    @Insert
    fun insertContinuous(continuous: ContinuousEntity)

    @Update
    fun updateDiscrete(discrete: DiscreteEntity)

    @Delete
    fun deleteDiscrete(discrete: DiscreteEntity)

    @Query("SELECT * FROM discrete WHERE id = :id")
    fun getDiscrete(id: Int): DiscreteEntity?

    @Insert
    fun insertDiscrete(discrete: DiscreteEntity)
}