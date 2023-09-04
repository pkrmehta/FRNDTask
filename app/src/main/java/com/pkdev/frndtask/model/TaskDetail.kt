package com.pkdev.frndtask.model


import com.google.gson.annotations.SerializedName

data class TaskDetail(
    @SerializedName("date")
    val date: String,
    val description: String,
    val title: String
)