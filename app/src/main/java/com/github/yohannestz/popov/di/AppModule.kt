package com.github.yohannestz.popov.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.github.yohannestz.popov.data.local.CallRepository
import com.github.yohannestz.popov.data.local.MessageRepository
import com.github.yohannestz.popov.data.local.db.NotificationRepositoryImpl
import com.github.yohannestz.popov.data.local.db.NotificationCacheDao
import com.github.yohannestz.popov.data.local.db.NotificationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideCallRepository(context: Context): CallRepository {
        return CallRepository(context)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(context: Context): MessageRepository {
        return MessageRepository(context)
    }

    @Provides
    @Singleton
    fun provideNotificationDatabase(context: Context): NotificationDatabase {
        return Room.databaseBuilder(
            context,
            NotificationDatabase::class.java,
            "Notification"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNotificationCacheDao(notificationDatabase: NotificationDatabase): NotificationCacheDao {
        return notificationDatabase.notificationCacheDao()
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(notificationCacheDao: NotificationCacheDao): NotificationRepositoryImpl {
        return NotificationRepositoryImpl(notificationCacheDao)
    }
}