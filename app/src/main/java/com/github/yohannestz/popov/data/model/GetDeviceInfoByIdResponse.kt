package com.github.yohannestz.popov.data.model

data class GetDeviceInfoByIdResponse(
    val result: List<DeviceInfoData>,
    val success: Boolean
)