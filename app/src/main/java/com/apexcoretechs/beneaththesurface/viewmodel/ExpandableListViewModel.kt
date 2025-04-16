package com.apexcoretechs.beneaththesurface.viewmodel

import androidx.lifecycle.ViewModel
import com.apexcoretechs.beneaththesurface.model.ExpandableItem
import com.apexcoretechs.beneaththesurface.ui.expandablelist.ExpandableListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExpandableListViewModel : ViewModel() {

    private val _state = MutableStateFlow(ExpandableListState())
    val state: StateFlow<ExpandableListState> = _state

    init {
        _state.value = ExpandableListState(
            items = listOf(
                ExpandableItem("Fruits", listOf("Apple", "Banana", "Mango")),
                ExpandableItem("Vegetables", listOf("Carrot", "Spinach", "Potato")),
                ExpandableItem("Dairy", listOf("Milk", "Cheese", "Yogurt"))
            )
        )
    }

    fun onItemToggle(index: Int) {
        val updated = _state.value.items.mapIndexed { i, item ->
            if (i == index) item.copy(isExpanded = !item.isExpanded) else item
        }
        _state.value = _state.value.copy(items = updated)
    }
}

