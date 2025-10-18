package com.example.testflow.domain.usecases

import com.example.testflow.domain.models.ExampleItem
import com.example.testflow.domain.models.ExampleItemsList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

interface GetExampleItemsUseCase {
    operator fun invoke(): Flow<ExampleItemsList>
}

class GetExampleItemsUseCaseImpl @Inject constructor() : GetExampleItemsUseCase {
    override operator fun invoke(): Flow<ExampleItemsList> =
        flow {
            delay(1.seconds)
            emit(
                ExampleItemsList(
                    ExampleItem(1, "First item"),
                    ExampleItem(2, "Second item"),
                    ExampleItem(3, "Third item")
                )
            )

            delay(2.seconds)

            emit(
                ExampleItemsList(
                    ExampleItem(id = 1, title = "First item (updated)"),
                    ExampleItem(id = 4, title = "Fourth item")
                )
            )
        }
}