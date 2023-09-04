package com.pkdev.frndtask.model

import com.google.gson.annotations.SerializedName

data class DeleteTaskRequest(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("task_id")
    val taskId: Int
)