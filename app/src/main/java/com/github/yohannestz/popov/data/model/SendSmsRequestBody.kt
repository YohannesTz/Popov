package com.github.yohannestz.popov.data.model

import com.google.gson.annotations.SerializedName

data class SendSmsRequestBody(
    @SerializedName("object_list") val smsList: List<Sms>
)
