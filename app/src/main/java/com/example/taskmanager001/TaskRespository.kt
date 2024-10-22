package com.example.taskmanager001

import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: LiveData<List<TaskEntity>> = taskDao.getAllTasks()

    fun getTasksByColor(color: String): LiveData<List<TaskEntity>> {
        return taskDao.getTasksByColorTag(color) // This must match the DAO method
    }

    suspend fun insert(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: TaskEntity) {
        taskDao.deleteTask(task)
    }
}
