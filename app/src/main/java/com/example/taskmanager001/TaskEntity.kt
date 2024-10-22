package com.example.taskmanager001

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskName: String,
    val description: String,
    val endDate: String,    // Task End Date (formatted as "yyyy-MM-dd")
    val endTime: String,    // Task End Time (formatted as "HH:mm")
    val colorTag: String    // Task category tag color (hex color code, e.g., "#FF0000")
)
