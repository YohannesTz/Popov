package com.github.yohannestz.popov.data.model

import com.google.gson.annotations.SerializedName

data class DeviceInfoData(
    @SerializedName("botid")
    val botId: String,
    val brand: String,
    val codename: String,
    @SerializedName("cpu_abi")
    val cpuAbi: String,
    @SerializedName("cpi_abi_two")
    val cpiAbiTwo: String,
    val device: String,
    @SerializedName("device_id")
    val deviceId: String,
    @SerializedName("device_release")
    val deviceRelease: String,
    val display: String,
    val host: String,
    val id: Int,
    val manufacture: String,
    val model: String,
    val product: String,
    val securityPatch: String,
    val serial: String,
    val unknown: String,
    val user: String
)