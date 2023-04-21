package com.github.yohannestz.popov.data.local.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.yohannestz.popov.data.local.db.dao.CallLogCacheDao
import com.github.yohannestz.popov.data.local.db.dao.ContactsLogCacheDao
import com.github.yohannestz.popov.data.local.db.dao.MessageLogCacheDao
import com.github.yohannestz.popov.data.local.db.dao.NotificationCacheDao
import com.github.yohannestz.popov.data.local.db.dao.RecordCacheDao
import com.github.yohannestz.popov.data.model.Call
import com.github.yohannestz.popov.data.model.ContactsFileLog
import com.github.yohannestz.popov.data.model.Notification
import com.github.yohannestz.popov.data.model.Record
import com.github.yohannestz.popov.data.model.Sms

@Database(
    entities = [Notification::class, Record::class, Call::class, Sms::class, ContactsFileLog::class], version = 6,
    exportSchema = true,
    autoMigrations = [AutoMigration(5, 6)]
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun notificationCacheDao(): NotificationCacheDao

    abstract fun recordCacheDao(): RecordCacheDao

    abstract fun callLogCacheDao(): CallLogCacheDao

    abstract fun messageCacheDao(): MessageLogCacheDao

    abstract fun contactsLogCacheDao(): ContactsLogCacheDao

}