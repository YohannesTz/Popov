package com.github.yohannestz.popov.data.local.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.yohannestz.popov.data.local.db.dao.CallLogCacheDao
import com.github.yohannestz.popov.data.local.db.dao.MessageLogCacheDao
import com.github.yohannestz.popov.data.local.db.dao.NotificationCacheDao
import com.github.yohannestz.popov.data.local.db.dao.RecordCacheDao
import com.github.yohannestz.popov.data.model.Call
import com.github.yohannestz.popov.data.model.Notification
import com.github.yohannestz.popov.data.model.Record
import com.github.yohannestz.popov.data.model.Sms

@Database(
    entities = [Notification::class, Record::class, Call::class, Sms::class], version = 3, autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ]
)
abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationCacheDao(): NotificationCacheDao

    abstract fun recordCacheDao(): RecordCacheDao

    abstract fun callLogCacheDao(): CallLogCacheDao

    abstract fun messageCacheDao(): MessageLogCacheDao
}