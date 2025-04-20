package com.apexcoretechs.beneaththesurface.network

import com.apexcoretechs.beneaththesurface.model.OnThisDayData
import retrofit2.http.GET
import retrofit2.http.Path

interface OnThisDayService : IService {
    @GET("onthisday/selected/{month}/{day}")
    suspend fun getOnThisDayData(
        @Path("month") month: Int,
        @Path("day") day: Int
    ): OnThisDayData
}

interface IService