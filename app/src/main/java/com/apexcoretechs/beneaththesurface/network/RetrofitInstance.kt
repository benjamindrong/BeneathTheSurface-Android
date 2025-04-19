package com.apexcoretechs.beneaththesurface.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://en.wikipedia.org/api/rest_v1/feed/"

    val api: OnThisDayService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OnThisDayService::class.java)
    }
}
