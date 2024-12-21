package com.rahul.safebite

import com.rahul.safebite.SnakeInfo.SnakeResponse
import com.rahul.safebite.SnakeInfo.iNaturalistResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SnakeService {
    @GET("taxa")
    suspend fun getSnakes(@Query("q") query: String): Response<iNaturalistResponse>
}

