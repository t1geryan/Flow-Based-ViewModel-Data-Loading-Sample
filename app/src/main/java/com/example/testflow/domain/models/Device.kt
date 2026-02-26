package com.example.testflow.domain.models

typealias DeviceId = Int

data class Device(
    val id: DeviceId,
    val name: String,
    val userId: Int,
)

data class DevicesList(
    val devices: List<Device>,
) : List<Device> by devices {

    constructor(vararg devices: Device) : this(devices.toList())
}