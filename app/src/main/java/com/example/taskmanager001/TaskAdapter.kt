package com.example.taskmanager001

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager001.databinding.ItemTaskBinding

class TaskAdapter(
    private var tasks: List<TaskEntity>,
    private val onEditClicked: (TaskEntity) -> Unit,
    private val onDeleteClicked: (TaskEntity) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskEntity) {
            binding.taskName.text = task.taskName
            binding.taskDate.text = "${binding.root.context.getString(R.string.task_end_date_label)} ${task.endDate} ${binding.root.context.getString(R.string.task_end_time_label)} ${task.endTime}"
            binding.taskDescription.text = task.description

            // Set the background color of the task based on the color tag
            try {
                val color = Color.parseColor(task.colorTag)
                binding.colorTag.setBackgroundColor(color)
            } catch (e: IllegalArgumentException) {
                binding.colorTag.setBackgroundColor(Color.parseColor("#FFA500")) // Fallback color if parsing fails
            }

            binding.editButton.setOnClickListener {
                onEditClicked(task)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClicked(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun setTasks(newTasks: List<TaskEntity>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
