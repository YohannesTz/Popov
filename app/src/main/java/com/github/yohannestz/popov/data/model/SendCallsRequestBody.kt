package com.github.yohannestz.popov.data.model

import com.google.gson.annotations.SerializedName

data class SendCallsRequestBody(
    @SerializedName("object_list")
    val callList: List<Call>
)
