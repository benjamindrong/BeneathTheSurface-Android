package com.apexcoretechs.beneaththesurface.ui.expandablelist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apexcoretechs.beneaththesurface.model.ExpandableItem
import com.apexcoretechs.beneaththesurface.model.OnThisDayData
import com.apexcoretechs.beneaththesurface.network.OnThisDayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ExpandableListViewModel : ViewModel() {

    private val _state = MutableStateFlow(ExpandableListState())
    val state: StateFlow<ExpandableListState> = _state

    private val repository = OnThisDayRepository()

    private val _onThisDayData = MutableLiveData<OnThisDayData>()
    val onThisDayData: LiveData<OnThisDayData> = _onThisDayData

    fun loadOnThisDayData(month: Int, day: Int) {
        viewModelScope.launch {
            try {
                val result = repository.fetchOnThisDayData(month, day)
                _onThisDayData.value = result
                val items = result.selected.map { selected ->
                    ExpandableItem(
                        title = selected.text,
                        year = selected.year.toString(),
                        pages = selected.pages
                    )
                }
                Log.i("loadOnThisDatData", "month $month day $day")
                _state.value = ExpandableListState(items = items)
            } catch (e: Exception) {
                Log.e("OnThisDay", "Error fetching data", e)
            }
        }
    }

    fun onItemToggle(index: Int) {
        val updated = _state.value.items.mapIndexed { i, item ->
            if (i == index) item.copy(isExpanded = !item.isExpanded) else item
        }
        _state.value = _state.value.copy(items = updated)
    }

    fun loadFromJson(json: String) {
        val parsed = Json.Default.decodeFromString<OnThisDayData>(json)

        val items = parsed.selected.map { selected ->
            ExpandableItem(
                title = selected.text,
                year = selected.year.toString(),
                pages = selected.pages
            )
        }

        _state.value = ExpandableListState(items = items)
    }
}