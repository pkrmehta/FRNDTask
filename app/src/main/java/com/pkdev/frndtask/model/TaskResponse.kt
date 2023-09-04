package com.pkdev.frndtask.model


import com.google.gson.annotations.SerializedName

data class TaskResponse(
    val tasks: List<Task>
)