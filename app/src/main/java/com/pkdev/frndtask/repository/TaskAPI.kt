package com.pkdev.frndtask.repository

import com.pkdev.frndtask.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TaskAPI {

    @POST("/api/getCalendarTaskList")
    fun getTasks(@Body payload: GetTaskRequest): Call<TaskResponse>

    @POST("/api/deleteCalendarTask")
    fun deleteTask(@Body payload: DeleteTaskRequest): Call<TaskGeneralResponse>

    @POST("/api/storeCalendarTask")
    fun storeTask(@Body payload: StoreTaskRequest): Call<TaskGeneralResponse>

}