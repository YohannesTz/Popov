package com.github.yohannestz.popov.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.yohannestz.popov.data.model.Notification

@Database(entities = [Notification::class], version = 1)
abstract class NotificationDatabase: RoomDatabase() {
    abstract fun notificationCacheDao(): NotificationCacheDao
}