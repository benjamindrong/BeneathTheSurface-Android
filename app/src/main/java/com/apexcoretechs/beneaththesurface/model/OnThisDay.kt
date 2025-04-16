package com.apexcoretechs.beneaththesurface.model

import kotlinx.serialization.Serializable

interface IShop {
    val route: String
}

@Serializable
data class OnThisDayData(
    val selected: List<Selected>,
    @Transient val timestamp: Long = 0,
    @Transient val formMonth: String = "00",
    @Transient val formDay: String = "00"
): IShop {
    companion object {
        const val getRoute = "/onThisDay"
        const val fullUrl = "https://en.wikipedia.org/api/rest_v1/feed/onthisday/selected"
    }
    override val route: String
        get() = getRoute
}

data class OnThisDayRequest(
    val month: Int,
    val day: Int
)

@Serializable
data class Page(
    val content_urls: ContentUrls? = null,
    val coordinates: Coordinates? = null,
    val description: String? = null,
    val description_source: String? = null,
    val dir: String? = null,
    val displaytitle: String? = null,
    val extract: String? = null,
    val extract_html: String? = null,
    val lang: String? = null,
    val namespace: Namespace? = null,
    val normalizedtitle: String? = null,
    val originalimage: Originalimage? = null,
    val pageid: Int? = null,// Article identifier
    val revision: String? = null,
    val thumbnail: Thumbnail? = null,
    val tid: String? = null, // Time-based UUID used for rendering content changes
    val timestamp: String? = null,
    val title: String? = null,
    val titles: Titles? = null,
    val type: String? = null,
    val wikibase_item: String? = null
)

@Serializable
data class Selected(
    val pages: List<Page>,
    val text: String,
    val year: Int? = 0
)

@Serializable
data class ContentUrls(
    val desktop: Desktop? = null,
    val mobile: Mobile? = null
)

@Serializable class Namespace(
    val id: Int,
    val text: String
)

@Serializable class Thumbnail(
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

@Serializable class Mobile(
    val edit: String? = null,
    val page: String? = null,
    val revisions: String? = null,
    val talk: String? = null
)

@Serializable class Titles(
    val canonical: String,
    val display: String,
    val normalized: String
)

@Serializable data class Coordinates(
    val lat: Double? = null,
    val lon: Double? = null
)

@Serializable class Originalimage(
    val height: Int,
    val source: String,
    val width: Int
)