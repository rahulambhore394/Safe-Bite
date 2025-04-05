package com.rahul.safebite.ExtraClasses

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface NyckelApi {
    @Multipart
    @POST("v1/functions/{csi45efb7ncs4tu3qzyg8kr30seqd3ew}/invoke")
    fun identifySnake(
        @Part file: MultipartBody.Part,
        @Header("Authorization") apiKey: String
    ): Call<ResponseBody>
}
