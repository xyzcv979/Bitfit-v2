package com.example.bitfit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_table")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "day") val day: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "descr") val descr: String?
)