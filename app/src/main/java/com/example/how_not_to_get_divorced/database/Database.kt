package com.example.how_not_to_get_divorced.database

import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.how_not_to_get_divorced.database.entities.AlarmEntity
import com.example.how_not_to_get_divorced.database.entities.ContinuousEntity
import com.example.how_not_to_get_divorced.database.entities.DiscreteEntity
import com.example.how_not_to_get_divorced.database.entities.TaskEntity
import com.example.how_not_to_get_divorced.model.Alarm

@Database(
    entities = [AlarmEntity::class, ContinuousEntity::class, DiscreteEntity::class, TaskEntity::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): Dao
}