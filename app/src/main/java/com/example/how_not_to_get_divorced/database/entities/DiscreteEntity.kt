package com.example.how_not_to_get_divorced.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discrete")
data class DiscreteEntity(
    @PrimaryKey var id: Int,
    val monday: Float,
    val tuesday: Float,
    val wednesday: Float,
    val thursday: Float,
    val friday: Float,
    val saturday: Float,
    val sunday: Float
)