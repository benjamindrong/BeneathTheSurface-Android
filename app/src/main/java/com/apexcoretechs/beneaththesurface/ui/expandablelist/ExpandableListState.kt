package com.apexcoretechs.beneaththesurface.ui.expandablelist

import com.apexcoretechs.beneaththesurface.model.ExpandableItem

data class ExpandableListState(
    val items: List<ExpandableItem> = emptyList(),
    val isTimeout: Boolean = false
)
