package com.example.testflow.domain.usecases

import com.example.testflow.domain.models.ExampleItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

interface GetExampleItemsUseCase {
    operator fun invoke(): Flow<List<ExampleItem>>
}

class GetExampleItemsUseCaseImpl @Inject constructor() : GetExampleItemsUseCase {
    private val flow = MutableStateFlow(
        listOf(
            ExampleItem(1, "Первый элемент"),
            ExampleItem(2, "Второй элемент"),
            ExampleItem(3, "Третий элемент")
        )
    )

    override operator fun invoke(): Flow<List<ExampleItem>> = flow
}