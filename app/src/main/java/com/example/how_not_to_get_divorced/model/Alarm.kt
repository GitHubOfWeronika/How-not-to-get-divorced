package com.example.how_not_to_get_divorced.model

import java.util.*

/**
 * Data structure of Alarm
 */
data class Alarm(
    var id: Int?,
    var name: String,
    var category: Category,
    var repetition: AlarmRepetition,
    var active: Boolean = true,
    var deleted: Boolean = false,
    var created: Date = Date()
)