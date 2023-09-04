package com.pkdev.frndtask.repository

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    const val baseUrl = "http://dev.frndapp.in:8080/"


    fun getInstance(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(loggingInterceptor)

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}