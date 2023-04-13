package com.github.yohannestz.popov.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Record")
data class Record(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timeStamp: String,
    val fileName: String,
    val isUploaded: Boolean
)
