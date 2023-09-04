package com.pkdev.frndtask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkdev.frndtask.model.StoreTaskRequest
import com.pkdev.frndtask.model.TaskGeneralResponse
import com.pkdev.frndtask.model.TaskResponse
import com.pkdev.frndtask.repository.TaskRepo
import kotlinx.coroutines.launch

class TaskViewModel: ViewModel() {

    private val taskRepo = TaskRepo()
    val storeTaskResponse : LiveData<TaskGeneralResponse>
        get() {
            return taskRepo.postTaskResponse
        }
    val deleteTaskResponse: LiveData<TaskGeneralResponse>
        get() {
            return taskRepo.deleteTaskResponse
        }

    fun getTask(): MutableLiveData<TaskResponse>{
        viewModelScope.launch {
            taskRepo.getTask(514)
        }
        return taskRepo.taskList
    }

    fun storeTask(payload: StoreTaskRequest){
        viewModelScope.launch {
            taskRepo.postTask(payload)
        }
    }

    fun deleteTask(taskId: Int){
        viewModelScope.launch {
            taskRepo.deleteTask(514, taskId)
        }
    }





}