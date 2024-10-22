package com.example.taskmanager001

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM task_table WHERE colorTag = :color ORDER BY endDate ASC, endTime ASC")
    fun getTasksByColorTag(color: String): LiveData<List<TaskEntity>> // This method must exist

    @Query("SELECT * FROM task_table ORDER BY endDate ASC, endTime ASC")
    fun getAllTasks(): LiveData<List<TaskEntity>>
}
