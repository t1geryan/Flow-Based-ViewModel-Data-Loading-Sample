package com.example.testflow.ui.features.example

import androidx.lifecycle.viewModelScope
import com.example.testflow.core.mvi.IntentReceiver
import com.example.testflow.core.mvi.StateHolderViewModel
import com.example.testflow.domain.models.ExampleItemsList
import com.example.testflow.domain.usecases.GetExampleItemsUseCase
import com.example.testflow.ui.features.example.ExampleViewModel.ExampleTrigger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val getItemsUseCase: GetExampleItemsUseCase,
) : StateHolderViewModel<ExampleState, ExampleTrigger>(ExampleState()),
    IntentReceiver<ExampleIntent> {

    override val dataFlow: Flow<Any>
        get() = getItemsUseCase()

    override suspend fun handleDataUpdates(data: Any) {
        when (data) {
            is ExampleItemsList -> {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        items = data.items.map { item ->
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

            is ExampleTrigger.SelectItem -> {
                updateState { state ->
                    state.copy(
                        items = state.items.map {
                            if (it.item.id == data.itemId) it.copy(isSelected = it.isSelected.not()) else it
                        }
                    )
                }
            }
        }
    }

    override fun receiveIntent(intent: ExampleIntent) {
        when (intent) {
            is ExampleIntent.ClickExample -> onItemClicked(intent.id)
        }
    }

    private fun onItemClicked(id: Int) {
        viewModelScope.launch {
            sendTrigger(ExampleTrigger.SelectItem(id))
        }
    }

    sealed interface ExampleTrigger {
        data class SelectItem(val itemId: Int) : ExampleTrigger
    }
}
