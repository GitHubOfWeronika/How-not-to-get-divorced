package com.example.how_not_to_get_divorced

import android.app.*
import android.app.Application
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import com.example.how_not_to_get_divorced.receivers.ContinuousAlarmReceiver
import com.example.how_not_to_get_divorced.receivers.DiscreteAlarmReceiver


class Application : Application() {
    val timeForDiscrete = 8 * 3600 // czas na dyskretny alarm

    companion object {
        val CHANNEL_ID = "notifications"
        val continuousAlarmId = 0
        val discreteAlarmId = 1
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        setBackgroundService()
    }

    //Tworzy kanał powiadomień
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Reminders about tasks",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notifications about new tasks to do"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun setBackgroundService() {
        val alarmMgr = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val continuousIntent = Intent(this, ContinuousAlarmReceiver::class.java)
        continuousIntent.action = "CONTINUOUS_ALARM" // nadajemy unikatową nazwę
        if (PendingIntent.getBroadcast( //Sprawdzamy czy powiadomienia zostały już ustawione
                this,
                continuousAlarmId,
                continuousIntent,
                PendingIntent.FLAG_NO_CREATE
            ) == null
        ) {
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                continuousAlarmId,
                continuousIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val fiveMinutes: Long = 5 * 60 * 1000
            alarmMgr.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                fiveMinutes,
                pendingIntent
            )
        }
        val discreteIntent = Intent(this, DiscreteAlarmReceiver::class.java)
        discreteIntent.action = "DISCRETE_ALARM"  // nadajemy unikatową nazwę
        if (PendingIntent.getBroadcast( //Sprawdzamy czy powiadomienia zostały już ustawione
                this,
                discreteAlarmId,
                discreteIntent,
                PendingIntent.FLAG_NO_CREATE
            ) == null
        ) {
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                discreteAlarmId,
                discreteIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.MILLISECONDS_IN_DAY, timeForDiscrete * 1000)
            }
            alarmMgr.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }
}