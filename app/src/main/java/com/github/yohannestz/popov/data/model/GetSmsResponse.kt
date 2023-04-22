package com.github.yohannestz.popov.data.model

data class GetSmsResponse(
    val result: List<SmsData>,
    val success: Boolean
)