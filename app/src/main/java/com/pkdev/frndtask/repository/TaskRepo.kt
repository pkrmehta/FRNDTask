package com.pkdev.frndtask.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pkdev.frndtask.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepo {

    private var taskApi: TaskAPI = RetrofitHelper.getInstance().create(TaskAPI::class.java)

    var taskList = MutableLiveData<TaskResponse>()
    var postTaskResponse =  MutableLiveData<TaskGeneralResponse>()
    var deleteTaskResponse =  MutableLiveData<TaskGeneralResponse>()

    suspend fun getTask(userId: Int){

        val payload = GetTaskRequest(userId)
        taskApi.getTasks(payload).enqueue(object: Callback<TaskResponse>{
            override fun onResponse(
                call: Call<TaskResponse>,
                response: Response<TaskResponse>
            ) {
                taskList.postValue(response.body())
            }
            override fun onFailure(call: Call<TaskResponse>, t: Throwable) {
                taskList.postValue(null)
                Log.e("Task Repo", "Get Task API Failed: ${t.printStackTrace()}")
            }
        })
    }

    suspend fun postTask(payload: StoreTaskRequest){
        taskApi.storeTask(payload).enqueue(object: Callback<TaskGeneralResponse>{
            override fun onResponse(
                call: Call<TaskGeneralResponse>,
                response: Response<TaskGeneralResponse>
            ) {
                postTaskResponse.postValue(response.body())
            }
            override fun onFailure(call: Call<TaskGeneralResponse>, t: Throwable) {
                postTaskResponse.postValue(null)
                Log.e("Task Repo", "Post Task API Failed: ${t.printStackTrace()}")
            }
        })
    }

    suspend fun deleteTask(userId: Int, taskId: Int){

        val payload = DeleteTaskRequest(userId, taskId)

        taskApi.deleteTask(payload).enqueue(object : Callback<TaskGeneralResponse>{
            override fun onResponse(
                call: Call<TaskGeneralResponse>,
                response: Response<TaskGeneralResponse>
            ) {
                deleteTaskResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<TaskGeneralResponse>, t: Throwable) {
                deleteTaskResponse.postValue(null)
                Log.e("Task Repo", "Delete Task API Failed: ${t.printStackTrace()}")
            }
        })
    }


}