package com.example.how_not_to_get_divorced.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    val name: String,
    val category: Int,
    val active: Boolean = true,
    val deleted: Boolean = false,
    val created: Long = System.currentTimeMillis() / 1000L
)
