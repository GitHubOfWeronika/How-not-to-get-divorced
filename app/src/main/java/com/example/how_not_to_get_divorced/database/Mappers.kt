package com.example.how_not_to_get_divorced.database

import com.example.how_not_to_get_divorced.database.entities.FullAlarm
import com.example.how_not_to_get_divorced.database.entities.FullTask
import com.example.how_not_to_get_divorced.database.entities.TasksStatistics
import com.example.how_not_to_get_divorced.model.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Mappers {
    companion object {
        fun toAlarm(alarm: FullAlarm): Alarm {
            if (alarm.type == 1) {
                return Alarm(
                    alarm.id,
                    alarm.name,
                    Category.getById(alarm.category)!!,
                    AlarmRepetition.Continuous(
                        alarm.monday,
                        alarm.tuesday,
                        alarm.wednesday,
                        alarm.thursday,
                        alarm.friday,
                        alarm.saturday,
                        alarm.sunday
                    ),
                    alarm.active,
                    alarm.deleted,
                    Date(alarm.created*1000L)
                )
            } else {
                return Alarm(
                    alarm.id,
                    alarm.name,
                    Category.getById(alarm.category)!!,
                    AlarmRepetition.Continuous(
                        alarm.monday,
                        alarm.tuesday,
                        alarm.wednesday,
                        alarm.thursday,
                        alarm.friday,
                        alarm.saturday,
                        alarm.sunday
                    ),
                    alarm.active,
                    alarm.deleted,
                    Date(alarm.created*1000L)
                )
            }
        }

        fun toTask(alarm: FullTask): Task {
            if (alarm.type == 1) {
                return Task(
                    alarm.id,
                    Alarm(
                        alarm.alarm,
                        alarm.name,
                        Category.getById(alarm.category)!!,
                        AlarmRepetition.Continuous(
                            alarm.monday,
                            alarm.tuesday,
                            alarm.wednesday,
                            alarm.thursday,
                            alarm.friday,
                            alarm.saturday,
                            alarm.sunday
                        ),
                        alarm.active,
                        alarm.deleted,
                        Date(alarm.created*1000L)
                    ),
                    java.util.Date(alarm.date * 1000),
                    Completion.get(alarm.completion)
                )
            } else {
                return Task(
                    alarm.id,
                    Alarm(
                        alarm.alarm,
                        alarm.name,
                        Category.getById(alarm.category)!!,
                        AlarmRepetition.Discrete(
                            alarm.monday,
                            alarm.tuesday,
                            alarm.wednesday,
                            alarm.thursday,
                            alarm.friday,
                            alarm.saturday,
                            alarm.sunday
                        ),
                        alarm.active,
                        alarm.deleted,
                        Date(alarm.created*1000L)
                    ),
                    java.util.Date(alarm.date * 1000),
                    Completion.get(alarm.completion)
                )
            }
        }

        fun daysInPast(date: String): Int{
            val diff: Long = Date().time - SimpleDateFormat("yyyy-MM-dd").parse(date)!!.time
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
        }

        fun dateInPast(daysInPast: Int): String{
            return SimpleDateFormat("yyyy-MM-dd").format(Date(Date().time - daysInPast * 24 * 3600000))
        }

        fun statisticsToArray(data: List<TasksStatistics>, until: Date): Array<Map<Completion, Int>> {
            val timeSpan = daysInPast(SimpleDateFormat("yyyy-MM-dd").format(until)) + 1
            val result = Array(timeSpan.toInt()) { mutableMapOf(
                Completion.DONE to 0,
                Completion.FAILED to 0,
                Completion.WAITING to 0)
            }
            for (stat in data){
                val index = daysInPast(stat.date)
                when(stat.completion){
                    Completion.DONE.id -> result[index][Completion.DONE] = result[index][Completion.DONE]!!.inc()
                    Completion.FAILED.id -> result[index][Completion.FAILED] = result[index][Completion.FAILED]!!.inc()
                    Completion.WAITING.id -> result[index][Completion.WAITING] = result[index][Completion.WAITING]!!.inc()

                }
            }
            return result.toList().toTypedArray()
        }
    }
}