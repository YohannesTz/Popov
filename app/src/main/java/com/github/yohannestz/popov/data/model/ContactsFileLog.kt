package com.github.yohannestz.popov.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ContactsFileLog")
data class ContactsFileLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fileUrl: String,
    val loggedAt: String,
    val isUploaded:Boolean = false,
)
