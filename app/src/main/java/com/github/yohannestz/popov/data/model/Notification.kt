package com.github.yohannestz.popov.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notification (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "application_name") val applicationName: String,
    @ColumnInfo(name = "application_package") val applicationPackage: String,
    @ColumnInfo(name = "notification_time") val notificationTime: String,
    @ColumnInfo(name = "notification_tickertext") val notificationTickerText: String
)