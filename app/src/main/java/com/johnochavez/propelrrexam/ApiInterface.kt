package com.johnochavez.propelrrexam

import com.johnochavez.propelrrexam.model.ApiResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("v3/3939a583-762c-47e5-9889-f47e9e931462")
    suspend fun getResponse() : Response<ApiResponseModel>
}