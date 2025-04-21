package com.rahul.safebite.SnakeRescue

import retrofit2.http.GET
import retrofit2.http.Query

interface TomTomApi {
    @GET("search/2/search/snake.json")
    suspend fun getSnakeCatchers(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("radius") radius: Int = 80000,
        @Query("key") key: String = "DLf2nyLXSEK5OptkVFc6AGYFsNbJRxRF"
    ): TomTomResponse
}
