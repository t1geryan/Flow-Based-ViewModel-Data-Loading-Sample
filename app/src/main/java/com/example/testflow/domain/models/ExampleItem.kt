package com.example.testflow.domain.models

data class ExampleItem(
    val id: Int,
    val title: String,
)

data class ExampleItemsList(
    val items: List<ExampleItem>,
) {

    constructor(vararg items: ExampleItem) : this(items.toList())
}