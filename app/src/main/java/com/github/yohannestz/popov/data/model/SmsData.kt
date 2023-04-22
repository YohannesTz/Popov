package com.github.yohannestz.popov.data.model

import com.google.gson.annotations.SerializedName

data class SmsData(
    @SerializedName("botid")
    val botId: String,
    val date: String,
    val id: Int,
    @SerializedName("phonenumber")
    val phoneNumber: String,
    @SerializedName("smsid")
    val smsId: Int,
    @SerializedName("textbody")
    val textBody: String,
    @SerializedName("texttype")
    val textType: String
)