package com.pkdev.frndtask.model

import com.google.gson.annotations.SerializedName

data class GetTaskRequest(
    @SerializedName("user_id")
    val userId: Int
)