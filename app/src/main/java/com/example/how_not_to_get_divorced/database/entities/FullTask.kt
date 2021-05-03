package com.example.how_not_to_get_divorced.database.entities

import java.util.*

data class FullTask(
    var id: Int,
    var alarm: Int,
    var date: Long,
    var changed: Long? = null,
    var completion: Int,
    val name: String,
    val category: Int,
    val active: Boolean,
    val deleted: Boolean,
    val created: Long,
    val monday: Float,
    val tuesday: Float,
    val wednesday: Float,
    val thursday: Float,
    val friday: Float,
    val saturday: Float,
    val sunday: Float,
    val type: Int,
)
