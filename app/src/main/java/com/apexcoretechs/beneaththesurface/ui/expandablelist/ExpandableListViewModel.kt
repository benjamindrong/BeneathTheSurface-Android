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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ExpandableListViewModel : ViewModel() {

    private val _state = MutableStateFlow(ExpandableListState())
    val state: StateFlow<ExpandableListState> = _state

    private val onThisDayRepository = OnThisDayRepository()
    private val aiFormRepository = AIFormRepository()

    private val _onThisDayData = MutableLiveData<OnThisDayData>()
    val onThisDayData: LiveData<OnThisDayData> = _onThisDayData

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

    fun loadCombinedHistory(month: Int, day: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val formattedDate = "${month.toString().padStart(2, '0')}/${day.toString().padStart(2, '0')}"

                val aiFormData = AIFormData(
                    utcTimestamp = System.currentTimeMillis().toDouble(),
                    freeText = formattedDate,
                    month = month,
                    day = day,
                    date = formattedDate,
                    isFreeRide = true
                )

                // Make the API call (use your actual networking layer here)
                val chatCompletionData = aiFormRepository.fetchOnThisDayData(aiFormData)

                // Convert to ExpandableItem
                val chatItem = ExpandableItem(
                    title = "AI Insights",
                    year = "", // Or parse from data if applicable
                    pages = listOf(Page(extract = chatCompletionData.choices.firstOrNull()?.message?.content ?: "No data"))
                )

                // Get historical data too
                val result = onThisDayRepository.fetchOnThisDayData(month, day)
                _onThisDayData.value = result

                val items = result.selected.map {
                    ExpandableItem(
                        title = it.text,
                        year = it.year.toString(),
                        pages = it.pages
                    )
                }

                _state.value = ExpandableListState(items = listOf(chatItem) + items)

            } catch (e: Exception) {
                Log.e("loadCombinedHistory", "Error: ${e.localizedMessage}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun loadHistoryWithAI(month: Int, day: Int) {
        viewModelScope.launch {
            try {
                val onThisDayResult = onThisDayRepository.fetchOnThisDayData(month, day)

                val freeTextFormatted = "${month.toString().padStart(2, '0')}/${day.toString().padStart(2, '0')}"
                val aiForm = AIFormData(
                    utcTimestamp = System.currentTimeMillis().toDouble(),
                    date = freeTextFormatted,
                    month = month,
                    day = day,
                    isFreeRide = true,
                    freeText = freeTextFormatted
                )

                val chatResponse = RetrofitInstance.aiFormApi.submitForm(aiForm)

                // Convert OnThisDayData to ExpandableItems
                val historicalItems = onThisDayResult.selected.map {
                    ExpandableItem(
                        title = it.text,
                        year = it.year.toString(),
                        pages = it.pages
                    )
                }

                // Convert ChatCompletionData to ExpandableItem
                val aiItems = chatResponse.choices.map {
                    ExpandableItem(
                        title = "AI Insight",
                        year = "", // optional
                        pages = listOf(
                            Page(
                                extract = it.message.content,
                                title = "AI Response"
                            )
                        )
                    )
                }

                _state.value = ExpandableListState(items = historicalItems + aiItems)
            } catch (e: Exception) {
                Log.e("loadHistoryWithAI", "Failed to fetch or merge history", e)
            }
        }
    }

}