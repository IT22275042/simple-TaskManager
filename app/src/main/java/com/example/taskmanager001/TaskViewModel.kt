package com.example.taskmanager001

import androidx.lifecycle.*
import kotlinx.coroutines.launch

// TaskViewModel class
class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val allTasks: LiveData<List<TaskEntity>> = repository.allTasks

    fun getTasksByColor(color: String): LiveData<List<TaskEntity>> {
        return repository.getTasksByColor(color)
    }

    fun insert(task: TaskEntity) = viewModelScope.launch {
        repository.insert(task)
    }

    fun update(task: TaskEntity) = viewModelScope.launch {
        repository.update(task)
    }

    fun delete(task: TaskEntity) = viewModelScope.launch {
        repository.delete(task)
    }
}

// TaskViewModelFactory class
class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
