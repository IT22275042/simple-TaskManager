package com.example.taskmanager001

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager001.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter
    private var currentTaskId: Int? = null // Flag to track task being edited

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set button text and hints from strings.xml
        binding.addTaskButton.text = getString(R.string.add_task_button)
        binding.taskNameInput.hint = getString(R.string.task_name_hint)
        binding.taskDescriptionInput.hint = getString(R.string.task_description_hint)
        binding.taskEndDateInput.hint = getString(R.string.task_end_date_hint)
        binding.taskEndTimeInput.hint = getString(R.string.task_end_time_hint)

        // Set button color
        binding.addTaskButton.setBackgroundColor(getColor(R.color.button_color)) // Setting button color

        // Set up RecyclerView
        taskAdapter = TaskAdapter(emptyList(), this::onEditTask, this::onDeleteTask)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = taskAdapter

        // Initialize ViewModel
        val repository = TaskRepository(TaskDatabase.getDatabase(this).taskDao())
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(repository)).get(TaskViewModel::class.java)

        // Observe all tasks
        taskViewModel.allTasks.observe(this, { tasks ->
            taskAdapter.setTasks(tasks)
        })

        // Add a Color Spinner
        setupColorSpinner(binding.colorTagInput)

        // Set Date and Time Pickers
        binding.taskEndDateInput.setOnClickListener { showDatePicker() }
        binding.taskEndTimeInput.setOnClickListener { showTimePicker() }

        // Add new task or update existing task
        binding.addTaskButton.setOnClickListener {
            val taskName = binding.taskNameInput.text.toString()
            val taskDescription = binding.taskDescriptionInput.text.toString()
            val taskEndDate = binding.taskEndDateInput.text.toString()
            val taskEndTime = binding.taskEndTimeInput.text.toString()
            val taskColor = binding.colorTagInput.selectedItem.toString() // Get color from Spinner

            if (taskName.isNotEmpty() && taskEndDate.isNotEmpty() && taskEndTime.isNotEmpty()) {
                val newTask = TaskEntity(
                    id = currentTaskId ?: 0,  // Set task ID if editing, otherwise use 0
                    taskName = taskName,
                    description = taskDescription,
                    endDate = taskEndDate,
                    endTime = taskEndTime,
                    colorTag = taskColor
                )

                if (currentTaskId == null) {
                    taskViewModel.insert(newTask)  // Insert new task
                } else {
                    taskViewModel.update(newTask)  // Update existing task
                    currentTaskId = null  // Reset the flag after updating
                }

                clearInputFields()
            }
        }
    }

    // DatePickerDialog for date selection
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
                binding.taskEndDateInput.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    // TimePickerDialog for time selection
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.taskEndTimeInput.setText(formattedTime)
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    // Setting up color spinner
    private fun setupColorSpinner(spinner: Spinner) {
        val colors = resources.getStringArray(R.array.color_tags)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colors)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    // Editing an existing task
    private fun onEditTask(task: TaskEntity) {
        currentTaskId = task.id  // Set task ID to indicate we're editing this task
        binding.taskNameInput.setText(task.taskName)
        binding.taskDescriptionInput.setText(task.description)
        binding.taskEndDateInput.setText(task.endDate)
        binding.taskEndTimeInput.setText(task.endTime)

        // Set the color in the Spinner
        val colorPosition = (binding.colorTagInput.adapter as ArrayAdapter<String>).getPosition(task.colorTag)
        binding.colorTagInput.setSelection(colorPosition)
    }

    // Deleting a task
    private fun onDeleteTask(task: TaskEntity) {
        taskViewModel.delete(task)
    }

    // Clearing input fields after adding/updating task
    private fun clearInputFields() {
        binding.taskNameInput.text.clear()
        binding.taskDescriptionInput.text.clear()
        binding.taskEndDateInput.text.clear()
        binding.taskEndTimeInput.text.clear()
        currentTaskId = null  // Reset the editing task ID
    }

    private fun saveLastActivity() {
        val sharedPref = getSharedPreferences("TaskManagerPreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("LAST_ACTIVITY", MainActivity::class.java.simpleName)
            apply()
        }
    }

    private fun getLastActivity(): String? {
        val sharedPref = getSharedPreferences("TaskManagerPreferences", Context.MODE_PRIVATE)
        return sharedPref.getString("LAST_ACTIVITY", null)
    }
}
