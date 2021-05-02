package com.example.how_not_to_get_divorced.model

import java.util.*

data class Task(
    var id: Int?,
    var alarm: Alarm,
    var date: Date = Date(),
    var completion: Completion = Completion.WAITING,
)