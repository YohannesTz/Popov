package com.github.yohannestz.popov.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Call")
data class Call(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val callNumber: String?,
    val callName: String?,
    val callDate: String?,
    val callType: String?,
    val isCallNew: String?,
    val callDuration: String?,
    val isUploaded: Boolean = false
)
