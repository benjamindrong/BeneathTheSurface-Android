package com.apexcoretechs.beneaththesurface.network

import com.apexcoretechs.beneaththesurface.model.OnThisDayData

class OnThisDayRepository {
    suspend fun fetchOnThisDayData(month: Int, day: Int): OnThisDayData {
        return RetrofitInstance.api.getOnThisDayData(month, day)
    }
}
