package com.example.testflow.domain.models

data class Device(
    val id: Int,
    val name: String,
)

class DevicesList(
    val devices: List<Device>,
) {

    constructor(vararg devices: Device) : this(devices.toList())
}