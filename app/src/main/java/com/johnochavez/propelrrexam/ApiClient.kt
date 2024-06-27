package com.johnochavez.propelrrexam

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {


    private val MOCK_API_URL = "https://run.mocky.io/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(MOCK_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}