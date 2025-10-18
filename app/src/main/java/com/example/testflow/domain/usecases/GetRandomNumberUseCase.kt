package com.example.testflow.domain.usecases

import com.example.testflow.domain.models.GeneratedNumber
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

interface GetRandomNumberUseCase {
    suspend operator fun invoke(): Result<GeneratedNumber>
}

/**
 * Imitates API request
 */
class GetRandomNumberUseCaseImpl @Inject constructor() : GetRandomNumberUseCase {

    override suspend fun invoke(): Result<GeneratedNumber> = runCatching {
        delay(2.seconds)
        GeneratedNumber(Random.nextInt())
    }
}