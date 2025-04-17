package com.apexcoretechs.beneaththesurface.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

interface IShop {
    val route: String
}

@Serializable
data class OnThisDayData(
    val selected: List<Selected>,
    @Transient val timestamp: Long = 0,
    @Transient val formMonth: String = "00",
    @Transient val formDay: String = "00"
) : IShop {
    companion object {
        const val getRoute = "/onThisDay"
        const val fullUrl = "https://en.wikipedia.org/api/rest_v1/feed/onthisday/selected"
    }

    override val route: String
        get() = getRoute
}

@Serializable
data class OnThisDayRequest(
    val month: Int,
    val day: Int
)

@Serializable
data class Selected(
    val pages: List<Page>,
    val text: String,
    val year: Int? = 0
)

@Serializable
data class Page(
    @SerialName("content_urls") val contentUrls: ContentUrls? = null,
    val coordinates: Coordinates? = null,
    val description: String? = null,
    @SerialName("description_source") val descriptionSource: String? = null,
    val dir: String? = null,
    val displaytitle: String? = null,
    val extract: String? = null,
    @SerialName("extract_html") val extractHtml: String? = null,
    val lang: String? = null,
    val namespace: Namespace? = null,
    val normalizedtitle: String? = null,
    val originalimage: Originalimage? = null,
    val pageid: Int? = null,
    val revision: String? = null,
    val thumbnail: Thumbnail? = null,
    val tid: String? = null,
    val timestamp: String? = null,
    val title: String? = null,
    val titles: Titles? = null,
    val type: String? = null,
    @SerialName("wikibase_item") val wikibaseItem: String? = null
)

@Serializable
data class ContentUrls(
    val desktop: Desktop? = null,
    val mobile: Mobile? = null
)

@Serializable
data class Namespace(
    val id: Int,
    val text: String
)

@Serializable
data class Thumbnail(
    val height: Int,
    val source: String,
    val width: Int
)

@Serializable
data class Desktop(
    val edit: String? = null,
    val page: String? = null,
    val revisions: String? = null,
    val talk: String? = null
)

@Serializable
data class Mobile(
    val edit: String? = null,
    val page: String? = null,
    val revisions: String? = null,
    val talk: String? = null
)

@Serializable
data class Titles(
    val canonical: String,
    val display: String,
    val normalized: String
)

@Serializable
data class Coordinates(
    val lat: Double? = null,
    val lon: Double? = null
)

@Serializable
data class Originalimage(
    val height: Int,
    val source: String,
    val width: Int
)
