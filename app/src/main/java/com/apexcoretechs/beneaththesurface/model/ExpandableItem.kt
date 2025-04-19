package com.apexcoretechs.beneaththesurface.model

data class ExpandableItem(
    val title: String,
    val year: String,
    val pages: List<Page>,
    var isExpanded: Boolean = false
)
