package com.example.testflow.ui.features.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testflow.domain.models.ExampleItemsList
import com.example.testflow.domain.usecases.GetExampleItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val getItemsUseCase: GetExampleItemsUseCase,
) : ViewModel() {
    private var _state = ExampleState()

    private val selectItemListener = MutableSharedFlow<Int>()
    val state: StateFlow<ExampleState> = flow {
        merge(
            getItemsUseCase(), selectItemListener,
        ).collect { data ->
            when (data) {
                is ExampleItemsList -> {
                    _state = _state.copy(
                        isLoading = false,
                        items = data.items.map { item ->
                            ExampleItemUiState(
                                item = item,
                                isSelected = _state.items
                                    .firstOrNull { it.item.id == item.id }?.isSelected
                                    ?: false,
                            )
                        }
                    )
                }

                is Int -> {
                    _state = _state.copy(
                        items = _state.items.map {
                            if (it.item.id == data) it.copy(isSelected = it.isSelected.not()) else it
                        }
                    )
                }
            }

            emit(_state)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        _state,
    )

    fun handleIntent(exampleIntent: ExampleIntent) {
        when (exampleIntent) {
            is ExampleIntent.ClickExample -> onItemClicked(exampleIntent.id)
        }
    }

    private fun onItemClicked(id: Int) {
        viewModelScope.launch {
            selectItemListener.emit(id)
        }
    }
}