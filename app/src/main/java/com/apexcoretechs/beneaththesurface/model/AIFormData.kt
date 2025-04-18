package com.apexcoretechs.beneaththesurface.model

import kotlinx.serialization.Serializable

@Serializable
data class AIFormData(
    // TODO val id: String = uuid4().toString(),
    val userName: String = "",
    val userEmail: String = "",
    val description: String = "",
    val additionalPreferences: String = "",
    var orderId: String = "",
    var utcTimestamp: Double,
    var isPaid: Boolean = false,
    val wantInvitationDraft: Boolean = false,
    val date: String = "",
    val month: Int = 0,
    val day: Int = 0,
    val partyTime: String = "",
    val isFreeRide: Boolean = false,
    val freeText: String = ""
) : IShop {
    companion object {
        const val getPath = "/aiFormData"
    }
    override val route: String
        get() = getPath
}
