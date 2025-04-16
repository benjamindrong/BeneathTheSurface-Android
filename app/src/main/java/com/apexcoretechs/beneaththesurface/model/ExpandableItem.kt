package com.apexcoretechs.beneaththesurface.model

data class ExpandableItem(
    val title: String,
    val children: List<String>,
    var isExpanded: Boolean = false
)