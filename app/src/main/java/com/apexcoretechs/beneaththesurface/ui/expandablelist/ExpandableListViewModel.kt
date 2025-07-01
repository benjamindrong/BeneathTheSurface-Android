package com.apexcoretechs.beneaththesurface.ui.expandablelist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apexcoretechs.beneaththesurface.model.AIFormData
import com.apexcoretechs.beneaththesurface.model.ExpandableItem
import com.apexcoretechs.beneaththesurface.model.OnThisDayData
import com.apexcoretechs.beneaththesurface.model.Page
import com.apexcoretechs.beneaththesurface.network.AIFormRepository
import com.apexcoretechs.beneaththesurface.network.OnThisDayRepository
import com.apexcoretechs.beneaththesurface.network.RetrofitInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.Json

class ExpandableListViewModel : ViewModel() {

    private val _state = MutableStateFlow(ExpandableListState())
    val state: StateFlow<ExpandableListState> = _state

    private val onThisDayRepository = OnThisDayRepository()
    private val aiFormRepository = AIFormRepository()

    private val _onThisDayData = MutableLiveData<OnThisDayData>()
    val onThisDayData: LiveData<OnThisDayData> = _onThisDayData

    enum class LoadStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }

    private val _aiStatus = MutableStateFlow(LoadStatus.IDLE)
    val aiStatus: StateFlow<LoadStatus> = _aiStatus

    private val _historyStatus = MutableStateFlow(LoadStatus.IDLE)
    val historyStatus: StateFlow<LoadStatus> = _historyStatus

    fun loadOnThisDayData(month: Int, day: Int) {
        viewModelScope.launch {
            try {
                val result = onThisDayRepository.fetchOnThisDayData(month, day)
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isWaiting = MutableStateFlow(true)
    val isWaiting: StateFlow<Boolean> = _isWaiting

    fun loadCombinedHistory(month: Int, day: Int) {
        viewModelScope.launch {
            _state.value = ExpandableListState(items = emptyList())
            _isLoading.value = true
            _isWaiting.value = true
            _aiStatus.value = LoadStatus.LOADING
            _historyStatus.value = LoadStatus.LOADING

            val formattedDate = "$month/$day"
            val aiFormData = AIFormData(
                utcTimestamp = System.currentTimeMillis().toDouble(),
                freeText = formattedDate,
                month = month,
                day = day,
                date = formattedDate,
                isFreeRide = true
            )

            var aiItem: ExpandableItem? = null
            val historyItems = mutableListOf<ExpandableItem>()

            val aiCall = async {
                try {
                    val chatCompletionData = aiFormRepository.fetchOnThisDayData(aiFormData)
                    aiItem = ExpandableItem(
                        title = "AI Insights",
                        year = "",
                        pages = listOf(
                            Page(
                                extract = chatCompletionData.choices.firstOrNull()?.message?.content ?: "No data",
                            )
                        )
                    )
                    _aiStatus.value = LoadStatus.SUCCESS
                } catch (e: Exception) {
                    Log.e("AIData", "Failed to fetch AI data", e)
                    _aiStatus.value = LoadStatus.FAILED
                }
            }

            val historyCall = async {
                try {
                    val result = onThisDayRepository.fetchOnThisDayData(month, day)
                    _onThisDayData.value = result
                    historyItems.addAll(
                        result.selected.map {
                            ExpandableItem(
                                title = it.text,
                                year = it.year.toString(),
                                pages = it.pages
                            )
                        }
                    )
                    _historyStatus.value = LoadStatus.SUCCESS
                } catch (e: Exception) {
                    Log.e("OnThisDay", "Failed to fetch historical data", e)
                    _historyStatus.value = LoadStatus.FAILED
                }
            }

            val completed = withTimeoutOrNull(10_000) {
                aiCall.await()
                historyCall.await()
            }

            if (completed == null) {
                // Timeout occurred

                // Cancel remaining calls if they're still running
                if (aiCall.isActive) {
                    aiCall.cancelAndJoin()
                    _aiStatus.value = LoadStatus.FAILED
                }

                if (historyCall.isActive) {
                    historyCall.cancelAndJoin()
                    _historyStatus.value = LoadStatus.FAILED
                }

                val combinedItems = mutableListOf<ExpandableItem>()
                aiItem?.let { combinedItems.add(it) }
                combinedItems.addAll(historyItems)

                _state.value = ExpandableListState(
                    items = combinedItems,
                    isTimeout = combinedItems.isEmpty() // true only if both failed
                )
            } else {
                // Both succeeded within timeout
                val combinedItems = mutableListOf<ExpandableItem>()
                aiItem?.let { combinedItems.add(it) }
                combinedItems.addAll(historyItems)

                _state.value = ExpandableListState(items = combinedItems)
            }

            _isWaiting.value = false
            _isLoading.value = false
        }
    }

}