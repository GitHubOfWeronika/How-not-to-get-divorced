package com.example.how_not_to_get_divorced.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.how_not_to_get_divorced.model.Completion
import java.util.*

@Entity(
    tableName = "task",
    foreignKeys = [ForeignKey(
        entity = AlarmEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("alarm"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(index = true) var alarm: Int,
    var date: Long = System.currentTimeMillis() / 1000L,
    var changed: Date? = null,
    var completion: Int = Completion.WAITING.id,
)
