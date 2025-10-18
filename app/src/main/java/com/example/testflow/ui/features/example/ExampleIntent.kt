package com.example.testflow.ui.features.example

sealed interface ExampleIntent {

    data class ClickExample(val id: Int) : ExampleIntent

    data object GetRandomNumber : ExampleIntent
}