package com.github.yohannestz.popov.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Sms")
data class Sms(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val phoneNumber: String?,
    val textBody: String?,
    val textType: String?,
    val date: String?,
    val isUploaded: Boolean = false,
)
