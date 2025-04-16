package com.apexcoretechs.beneaththesurface.model

data class ExpandableItem(
    val text: String,
    val year: String,
    val children: List<String>,
    var isExpanded: Boolean = false
)