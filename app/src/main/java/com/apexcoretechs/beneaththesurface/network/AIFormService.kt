package com.apexcoretechs.beneaththesurface.network

import com.apexcoretechs.beneaththesurface.model.AIFormData
import com.apexcoretechs.beneaththesurface.model.ChatCompletionData
import retrofit2.http.Body
import retrofit2.http.POST

interface AIFormService : IService {
    @POST("/aiFormData")
    suspend fun submitForm(@Body data: AIFormData): ChatCompletionData
}