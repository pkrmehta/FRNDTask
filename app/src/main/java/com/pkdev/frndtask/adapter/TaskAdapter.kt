package com.pkdev.frndtask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pkdev.frndtask.databinding.ItemTaskBinding
import com.pkdev.frndtask.model.Task

class TaskAdapter(private val context: Context, private val list: List<Task>, val listener: TaskClickListener): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = list[position]
        holder.binding.textTitle.text = item.taskDetail.title
        holder.binding.textDesc.text = item.taskDetail.description
        holder.binding.btnDelete.setOnClickListener{
            listener.onTaskClick(item.taskId)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}