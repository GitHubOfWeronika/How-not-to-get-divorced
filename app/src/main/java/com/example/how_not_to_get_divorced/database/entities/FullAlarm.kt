package com.example.how_not_to_get_divorced.database.entities

data class FullAlarm(
    val id: Int,
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
