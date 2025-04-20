package com.apexcoretechs.beneaththesurface.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val ONTHISDAY_URL = "https://en.wikipedia.org/api/rest_v1/feed/"
    private const val AIFORM_URL = "http://192.168.1.4:3003/"

    val onthisdayApi: OnThisDayService by lazy {
        Retrofit.Builder()
            .baseUrl(ONTHISDAY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OnThisDayService::class.java)
    }
    val aiFormApi: AIFormService by lazy {
        Retrofit.Builder()
            .baseUrl(AIFORM_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AIFormService::class.java)
    }
}
