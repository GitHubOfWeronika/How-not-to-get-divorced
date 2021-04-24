package com.example.how_not_to_get_divorced.model

/**
 * Data structure of Alarm
 */
data class Alarm(
    val name: String,
    val category: Category,
    val repetition: AlarmRepetition
)