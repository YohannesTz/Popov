package com.github.yohannestz.popov.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.yohannestz.popov.data.model.Notification

@Dao
interface NotificationCacheDao {
    @Query("SELECT * FROM notification")
    suspend fun getAll(): List<Notification>

    @Query("SELECT * FROM notification WHERE application_package = :packageName")
    suspend fun getAllByPackageName(packageName: String): List<Notification>

    @Insert
    suspend fun insertNotification(notification: Notification)
}