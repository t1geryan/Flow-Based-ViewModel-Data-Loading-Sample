package com.example.testflow.ui.features.example

import androidx.lifecycle.viewModelScope
import com.example.testflow.core.mvi.IntentReceiver
import com.example.testflow.core.mvi.StateHolderViewModel
import com.example.testflow.domain.models.ExampleItemsList
import com.example.testflow.domain.usecases.GetExampleItemsUseCase
import com.example.testflow.domain.usecases.GetRandomNumberUseCase
import com.example.testflow.ui.features.example.ExampleViewModel.ExampleTrigger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val getRandomNumberUseCase: GetRandomNumberUseCase,
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

            is ExampleTrigger.SetLoading -> {
                updateState { state -> state.copy(isLoading = data.value) }
            }

            is ExampleTrigger.GenerateNumber -> generateNumber()
        }
    }

    override suspend fun onDataDemanded() {
        generateNumber()
    }

    override fun receiveIntent(intent: ExampleIntent) {
        viewModelScope.launch {
            when (intent) {
                is ExampleIntent.ClickExample -> sendTrigger(ExampleTrigger.SelectItem(intent.id))
                ExampleIntent.GetRandomNumber -> {
                    sendTrigger(ExampleTrigger.SetLoading(true))
                    sendTrigger(ExampleTrigger.GenerateNumber)
                }
            }
        }
    }

    private suspend fun generateNumber() {
        getRandomNumberUseCase()
            .onSuccess { generatedNumber ->
                updateState { state ->
                    state.copy(randomNumber = generatedNumber.value)
                }
            }.also {
                sendTrigger(ExampleTrigger.SetLoading(false))
            }
    }

    sealed interface ExampleTrigger {
        data class SelectItem(val itemId: Int) : ExampleTrigger

        data object GenerateNumber : ExampleTrigger

        data class SetLoading(val value: Boolean) : ExampleTrigger
    }
}
