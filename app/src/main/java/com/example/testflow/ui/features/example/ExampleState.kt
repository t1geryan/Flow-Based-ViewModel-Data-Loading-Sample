package com.example.testflow.ui.features.example

import com.example.testflow.domain.models.Device

data class ExampleItemUiState(
    val device: Device,
    val isSelected: Boolean,
)

data class ExampleState(
    val devices: List<ExampleItemUiState> = emptyList(),
    val isLoading: Boolean = true,
    val randomNumber: Int? = null,
)