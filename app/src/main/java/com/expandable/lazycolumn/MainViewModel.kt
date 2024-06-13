package com.expandable.lazycolumn

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun onHeaderClick(item: IResultList) {
        val items = _uiState.value.result.items
        val results = items.map {
            var result = it
            if (it.headerId == item.headerId) {
                result = when (it) {
                    is HeaderItem ->
                        it.copy(isExpanded = !item.isExpanded)

                    is ContentItem -> it.copy(isExpanded = !item.isExpanded)
                    else -> it
                }
            }
            result
        }
        _uiState.update {
            it.copy(result = ResultList(results.toList()))
        }
    }
}

@Stable
data class UiState(
    val result: ResultList = ResultList()
)

@Immutable
data class ResultList(
    val items: List<IResultList> = listOf(
        HeaderItem(headerId = 1),
        ContentItem(id = 1, headerId = 1),
        ContentItem(id = 2, headerId = 1),
        ContentItem(id = 3, headerId = 1),
        HeaderItem(headerId = 2),
        ContentItem(id = 4, headerId = 2),
        ContentItem(id = 5, headerId = 2),
        HeaderItem(headerId = 3),
        ContentItem(id = 6, headerId = 3),
        ContentItem(id = 7, headerId = 3),
        ContentItem(id = 8, headerId = 3),
        ContentItem(id = 9, headerId = 3),
    )
)

@Stable
data class HeaderItem(
    val name: String = "Name",
    override val headerId: Int,
    override val isExpanded: Boolean = false,
) : IResultList

@Stable
data class ContentItem(
    val id: Int,
    override val headerId: Int,
    override val isExpanded: Boolean = false,
    val content: String = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
) : IResultList

interface IResultList {
    val isExpanded: Boolean
    val headerId: Int
}