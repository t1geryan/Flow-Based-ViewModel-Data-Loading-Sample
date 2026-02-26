package com.example.testflow.domain.usecases

import com.example.testflow.domain.models.User
import com.example.testflow.domain.models.UsersList
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

interface GetUsersUseCase {
    operator fun invoke(): Flow<UsersList>
}

class GetUsersUseCaseImpl @Inject constructor() : GetUsersUseCase {

    override fun invoke(): Flow<UsersList> = flow {
        delay(2.seconds)
        emit(
            UsersList(
                User(id = 1, name = "First User"),
                User(id = 2, name = "Second User"),
            )
        )

        delay(3.seconds)

        emit(
            UsersList(
                User(id = 1, name = "First User"),
                User(id = 2, name = "Second User"),
                User(id = 3, name = "Third User"),
            )
        )
    }
}
