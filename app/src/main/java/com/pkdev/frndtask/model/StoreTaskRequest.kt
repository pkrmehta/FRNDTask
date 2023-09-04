package com.pkdev.frndtask.model

import com.google.gson.annotations.SerializedName

data class StoreTaskRequest (
    @SerializedName("user_id")
    val userId: Int,
    val task: TaskRequest
    )