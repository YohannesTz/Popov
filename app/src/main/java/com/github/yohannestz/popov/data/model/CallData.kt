package com.github.yohannestz.popov.data.model

import com.google.gson.annotations.SerializedName

data class CallData(
    @SerializedName("botid")
    val botId: String,
    val callDuration: String,
    @SerializedName("calldate")
    val callDate: String,
    @SerializedName("callid")
    val callId: Int,
    @SerializedName("callname")
    val callName: String,
    @SerializedName("callnumber")
    val callNumber: String,
    @SerializedName("calltype")
    val callType: String,
    val id: Int,
    val isCallNew: String
)