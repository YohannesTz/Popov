package com.github.yohannestz.popov.ui.calls

import com.github.yohannestz.popov.data.model.Call

data class CallScreenState(
    val callLogList: List<Call> = emptyList(),
    val isLoading:Boolean = false
)
