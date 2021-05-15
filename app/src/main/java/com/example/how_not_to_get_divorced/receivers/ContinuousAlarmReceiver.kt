package com.example.how_not_to_get_divorced.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.how_not_to_get_divorced.Application
import com.example.how_not_to_get_divorced.database.DBAccess
import com.example.how_not_to_get_divorced.model.Alarm
import com.example.how_not_to_get_divorced.model.AlarmRepetition
import com.example.how_not_to_get_divorced.model.Task
import java.time.*
import kotlin.random.Random

class ContinuousAlarmReceiver: BroadcastReceiver() {
    val dayBegin = 8 * 3600 // początke dnia w sekundach
    val dayEnd = 20 * 3600 // koniec dnia w sekunadch
    val period = 5 * 60 // okres pomiędzy wywołaniami
    val firingPerDay = (dayEnd - dayBegin) / period // ilosć wywołań na dzień
    override fun onReceive(context: Context, intent: Intent) {
        val dayOfWeek = LocalDate.now().dayOfWeek
        val nowZoned = ZonedDateTime.now()
        val midnight = nowZoned.toLocalDate().atStartOfDay(nowZoned.zone).toInstant()
        val duration = Duration.between(midnight, Instant.now())
        val seconds: Long = duration.seconds
        if (seconds in dayBegin..dayEnd) {
            val db = DBAccess(context)
            Thread {
                for (alarm in db.getContinuousAlarmsForNotification()) {
                    val chance = when (dayOfWeek){
                        DayOfWeek.MONDAY -> (alarm.repetition as AlarmRepetition.Continuous).monday / firingPerDay
                        DayOfWeek.TUESDAY -> (alarm.repetition as AlarmRepetition.Continuous).tuesday / firingPerDay
                        DayOfWeek.WEDNESDAY -> (alarm.repetition as AlarmRepetition.Continuous).wednesday / firingPerDay
                        DayOfWeek.THURSDAY -> (alarm.repetition as AlarmRepetition.Continuous).thursday / firingPerDay
                        DayOfWeek.FRIDAY -> (alarm.repetition as AlarmRepetition.Continuous).friday / firingPerDay
                        DayOfWeek.SATURDAY -> (alarm.repetition as AlarmRepetition.Continuous).saturday / firingPerDay
                        DayOfWeek.SUNDAY -> (alarm.repetition as AlarmRepetition.Continuous).sunday / firingPerDay
                    }
                    if (Random.nextFloat() < chance){ // losowanie
                        fireAlarm(alarm, db, context)
                    }
                }
            }.start()
        }
    }

    //Wstaw zadanie do bazy i wyświetl powiadomienie
    private fun fireAlarm(alarm: Alarm, db: DBAccess, context: Context){
        val task = Task(
            0,
            alarm
        )
        db.insertTask(task)
        val manager = NotificationManagerCompat.from(context)
        val notification = NotificationCompat.Builder(context, Application.CHANNEL_ID)
            .setSmallIcon(alarm.category.image)
            .setContentTitle("New Task")
            .setContentText(alarm.name)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        val id = when (alarm.id){
            null -> (System.currentTimeMillis() % 1048576).toInt()
            else -> alarm.id!! + (System.currentTimeMillis() % 1048576).toInt()
        }
        manager.notify(id, notification)
    }
}