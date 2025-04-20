package com.apexcoretechs.beneaththesurface.network

import com.apexcoretechs.beneaththesurface.model.AIFormData
import com.apexcoretechs.beneaththesurface.model.ChatCompletionData
import com.apexcoretechs.beneaththesurface.model.OnThisDayData

class AIFormRepository {
    suspend fun fetchOnThisDayData(data: AIFormData): ChatCompletionData {
        return RetrofitInstance.aiFormApi.submitForm(data)
    }
}
