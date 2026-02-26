package com.example.testflow.ui.features.example

import androidx.lifecycle.viewModelScope
import com.example.testflow.core.mvi.IntentReceiver
import com.example.testflow.core.mvi.StateHolderViewModel
import com.example.testflow.domain.models.DevicesList
import com.example.testflow.domain.models.UsersList
import com.example.testflow.domain.usecases.GetDevicesUseCase
import com.example.testflow.domain.usecases.GetRandomNumberUseCase
import com.example.testflow.domain.usecases.GetUsersUseCase
import com.example.testflow.ui.features.example.ExampleViewModel.ExampleTrigger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val getRandomNumberUseCase: GetRandomNumberUseCase,
    private val getDevicesUseCase: GetDevicesUseCase,
    private val getUsersUseCase: GetUsersUseCase,
) : StateHolderViewModel<ExampleState, ExampleTrigger>(ExampleState()),
    IntentReceiver<ExampleIntent> {

    // Using combine to make sure that users and devices will exist at the same time
    // Instead of flatMapMerge we can use new class-wrapper to send this two lists to handleDataUpdates as single variable
    // combine(A,B) {a, b -> Wrapper(a,b) }
    // handleDataUpdates(data: Any) {
    //     when(data) {
    //         is Wrapper -> {...}
    //     }
    // }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val dataFlow: Flow<Any>
        get() = combine(getUsersUseCase(), getDevicesUseCase()) { first, second ->
            listOf(first, second)
        }.flatMapMerge {
            it.asFlow()
        }

    override fun handleDataUpdates(data: Any) {
        when (data) {
            is UsersList -> {
                updateState { state ->
                    state.copy(
                        items = data.map { user ->
                            ExampleItemUiState(
                                user = user,
                                devices = state.items
                                    .firstOrNull { it.user.id == user.id }
                                    ?.devices
                                    ?: emptyList()
                            )
                        }
                    )
                }
            }

            is DevicesList -> {
                updateState { state ->
                    state.copy(
                        items = state.items.map { item ->
                            item.copy(
                                devices = data.devices.filter { it.userId == item.user.id }
                                    .map { device ->
                                        ExampleDeviceItemUiState(
                                            device = device,
                                            isSelected = state.devices
                                                .firstOrNull { it.device.id == device.id }?.isSelected
                                                ?: false,
                                        )
                                    }
                            )
                        },
                    )
                }
            }

            is ExampleTrigger.SelectItem -> {
                updateState { state ->
                    state.copy(
                        items = state.items.map { item ->
                            item.copy(
                                devices = item.devices.map { deviceItem ->
                                    deviceItem
                                        .takeIf { deviceItem.device.id == data.itemId }
                                        ?.copy(isSelected = deviceItem.isSelected.not())
                                        ?: deviceItem
                                },
                            )
                        }
                    )
                }
            }

            is ExampleTrigger.SetLoading -> {
                updateState { state -> state.copy(isLoading = data.value) }
            }

            is ExampleTrigger.SetGeneratedNumber -> {
                updateState { state -> state.copy(randomNumber = data.number)}
            }
        }
    }

    override fun receiveIntent(intent: ExampleIntent) {
        viewModelScope.launch {
            when (intent) {
                is ExampleIntent.ClickExample -> sendTrigger(ExampleTrigger.SelectItem(intent.id))
                ExampleIntent.GetRandomNumber -> generateNumber()
            }
        }
    }

    private fun generateNumber() {
        sendTrigger(ExampleTrigger.SetLoading(true))
        viewModelScope.launch {
            getRandomNumberUseCase()
                .onSuccess { generatedNumber ->
                    sendTrigger(ExampleTrigger.SetGeneratedNumber(generatedNumber.value))
                }.also {
                    sendTrigger(ExampleTrigger.SetLoading(false))
                }
        }
    }

    sealed interface ExampleTrigger {
        data class SelectItem(val itemId: Int) : ExampleTrigger

        data class SetGeneratedNumber(val number: Int) : ExampleTrigger

        data class SetLoading(val value: Boolean) : ExampleTrigger
    }
}
