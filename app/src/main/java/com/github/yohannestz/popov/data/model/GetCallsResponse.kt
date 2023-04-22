package com.github.yohannestz.popov.data.model

data class GetCallsResponse(
    val result: List<CallData>,
    val success: Boolean
)