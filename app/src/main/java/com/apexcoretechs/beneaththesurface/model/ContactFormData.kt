package com.apexcoretechs.beneaththesurface.model

import kotlinx.serialization.Serializable

// commonMain
@Serializable
data class ContactFormData(
    val name: String = "",
    val email: String = "",
    val message: String = "",
    // TODO val id: String = unique id
    var utcTimestamp: Double,
): IShop {
    companion object {
        const val getPath = "/contactFormData"
    }
    override val route: String
        get() = getPath
}