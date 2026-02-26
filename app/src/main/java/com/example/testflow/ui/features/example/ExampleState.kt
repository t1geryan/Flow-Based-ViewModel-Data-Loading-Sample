package com.example.testflow.ui.features.example

import androidx.compose.runtime.Immutable
import com.example.testflow.domain.models.Device
import com.example.testflow.domain.models.User

@Immutable
data class ExampleItemUiState(
    val user: User,
    val devices: List<ExampleDeviceItemUiState>,
)

@Immutable
data class ExampleDeviceItemUiState(
    val device: Device,
    val isSelected: Boolean,
)

@Immutable
data class ExampleState(
    val items: List<ExampleItemUiState> = emptyList(),
    val isLoading: Boolean = true,
    val randomNumber: Int? = null,
) {

    val devices: List<ExampleDeviceItemUiState>
        get() = items
            .flatMap { it.devices }
}