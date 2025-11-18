package com.example.testflow.domain.usecases

import com.example.testflow.domain.models.Device
import com.example.testflow.domain.models.DevicesList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

interface GetDevicesUseCase {
    operator fun invoke(): Flow<DevicesList>
}

class GetDevicesUseCaseImpl @Inject constructor() : GetDevicesUseCase {
    override fun invoke(): Flow<DevicesList> = flow {
        delay(1.seconds)
        emit(
            DevicesList(
                Device(id = 1, name = "First device"),
                Device(id = 2, name = "Second device"),
                Device(id = 3, name = "Third device")
            )
        )

        delay(5.seconds)

        emit(
            DevicesList(
                Device(id = 1, name = "First device (updated)"),
                Device(id = 4, name = "Fourth device")
            )
        )
    }

}