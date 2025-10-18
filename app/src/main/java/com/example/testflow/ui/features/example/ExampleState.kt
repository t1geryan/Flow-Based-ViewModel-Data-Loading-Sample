package com.example.testflow.ui.features.example

import com.example.testflow.domain.models.ExampleItem

data class ExampleItemUiState(
    val item: ExampleItem,
    val isSelected: Boolean,
)

data class ExampleState(
    val items: List<ExampleItemUiState> = emptyList(),
    val isLoading: Boolean = true,
)