package com.example.testflow.ui.features.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testflow.domain.usecases.GetExampleItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val getItemsUseCase: GetExampleItemsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ExampleState())
    val state: StateFlow<ExampleState> = _state.asStateFlow()

    init {
        loadItems()
    }

    fun handleIntent(exampleIntent: ExampleIntent) {
        when (exampleIntent) {
            is ExampleIntent.ClickExample -> onItemClicked(exampleIntent.id)
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            getItemsUseCase().collect { items ->
                _state.update { state ->
                    state.copy(
                        items = items.map { item ->
                            ExampleItemUiState(
                                item = item,
                                isSelected = state.items
                                    .firstOrNull { it.item.id == item.id }?.isSelected
                                    ?: false,
                            )
                        }
                    )
                }
            }
        }
    }

    private fun onItemClicked(id: Int) {
        _state.update { current ->
            current.copy(
                items = current.items.map { uiItem ->
                    if (uiItem.item.id == id) {
                        uiItem.copy(isSelected = !uiItem.isSelected)
                    } else uiItem
                }
            )
        }
    }
}