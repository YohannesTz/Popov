package com.github.yohannestz.popov.data.model

data class DeviceInfo(
    val release: String,
    val codeName: String,
    val securityPatch: String = "NOT AVAILABLE",
    val device: String,
    val model: String,
    val product: String,
    val brand: String,
    val display: String,
    val cpuAbi: String,
    val cpuAbiTwo: String,
    val unknown: String,
    val hardware: String,
    val id: String,
    val manufacture: String,
    val serial: String,
    val user: String,
    val host: String
)
