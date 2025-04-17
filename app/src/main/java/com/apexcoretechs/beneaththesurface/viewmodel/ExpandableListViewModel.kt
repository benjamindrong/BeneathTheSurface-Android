package com.apexcoretechs.beneaththesurface.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.apexcoretechs.beneaththesurface.model.ExpandableItem
import com.apexcoretechs.beneaththesurface.model.OnThisDayData
import com.apexcoretechs.beneaththesurface.ui.expandablelist.ExpandableListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json

class ExpandableListViewModel : ViewModel() {

    private val _state = MutableStateFlow(ExpandableListState())
    val state: StateFlow<ExpandableListState> = _state

//    init {
//        val parsed = Json.decodeFromString<OnThisDayData>(json)
//
//        val items = parsed.selected.map { selected ->
//            val children = selected.pages.map { page ->
//                "${page.title ?: "Untitled"} - ${page.extract ?: "No description"}"
//            }
//            ExpandableItem(
//                title = selected.year?.toString() ?: "Unknown Year",
//                children = children
//            )
//        }
//        _state.value = ExpandableListState(items = items)
//    }

    fun onItemToggle(index: Int) {
        val updated = _state.value.items.mapIndexed { i, item ->
            if (i == index) item.copy(isExpanded = !item.isExpanded) else item
        }
        _state.value = _state.value.copy(items = updated)
    }
    fun loadJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    fun loadFromJson(json: String) {
        val parsed = Json.decodeFromString<OnThisDayData>(json)

        val items = parsed.selected.map { selected ->
            val children = selected.pages.map { page ->
                page.title ?: "Untitled"
            }

            ExpandableItem(
                title = selected.text,
                year = selected.year.toString(),
                pages = selected.pages
            )
        }

        _state.value = ExpandableListState(items = items)
    }

}

fun loadJsonFromAssets(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}



