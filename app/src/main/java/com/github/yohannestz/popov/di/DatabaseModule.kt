package com.github.yohannestz.popov.di

import android.content.Context
import androidx.room.Room
import com.github.yohannestz.popov.data.local.db.ApplicationDatabase
import com.github.yohannestz.popov.data.local.db.dao.CallLogCacheDao
import com.github.yohannestz.popov.data.local.db.dao.ContactsLogCacheDao
import com.github.yohannestz.popov.data.local.db.dao.MessageLogCacheDao
import com.github.yohannestz.popov.data.local.db.dao.NotificationCacheDao
import com.github.yohannestz.popov.data.local.db.dao.RecordCacheDao
import com.github.yohannestz.popov.data.local.db.impl.ContactsFileLogRepositoryImpl
import com.github.yohannestz.popov.data.local.db.impl.RecordRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideApplicationDatabase(context: Context): ApplicationDatabase {
        return Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            "Notification"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNotificationCacheDao(applicationDatabase: ApplicationDatabase): NotificationCacheDao {
        return applicationDatabase.notificationCacheDao()
    }

    @Provides
    @Singleton
    fun provideRecordCacheDao(applicationDatabase: ApplicationDatabase): RecordCacheDao {
        return applicationDatabase.recordCacheDao()
    }

    @Provides
    @Singleton
    fun provideCallLogCacheDao(applicationDatabase: ApplicationDatabase): CallLogCacheDao {
        return applicationDatabase.callLogCacheDao()
    }

    @Provides
    @Singleton
    fun provideMessageLogCacheDao(applicationDatabase: ApplicationDatabase): MessageLogCacheDao {
        return applicationDatabase.messageCacheDao()
    }

    @Provides
    @Singleton
    fun provideContactsFileLogCacheDao(applicationDatabase: ApplicationDatabase): ContactsLogCacheDao {
        return applicationDatabase.contactsLogCacheDao()
    }

    @Provides
    fun provideRecordRepository(recordCacheDao: RecordCacheDao): RecordRepositoryImpl {
        return RecordRepositoryImpl(recordCacheDao)
    }

    @Provides
    @Singleton
    fun provideContactsFileLogRepository(contactsFileLogCacheDao: ContactsLogCacheDao): ContactsFileLogRepositoryImpl {
        return ContactsFileLogRepositoryImpl(contactsFileLogCacheDao)
    }
}