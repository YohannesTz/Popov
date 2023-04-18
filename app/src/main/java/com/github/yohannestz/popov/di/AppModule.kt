package com.github.yohannestz.popov.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.github.yohannestz.popov.data.local.CallRepository
import com.github.yohannestz.popov.data.local.MessageRepository
import com.github.yohannestz.popov.data.local.db.*
import com.github.yohannestz.popov.data.local.db.dao.NotificationCacheDao
import com.github.yohannestz.popov.data.local.db.dao.RecordCacheDao
import com.github.yohannestz.popov.data.local.db.NotificationDatabase
import com.github.yohannestz.popov.data.local.db.dao.CallLogCacheDao
import com.github.yohannestz.popov.data.local.db.dao.MessageLogCacheDao
import com.github.yohannestz.popov.data.local.db.impl.NotificationRepositoryImpl
import com.github.yohannestz.popov.data.local.db.impl.RecordRepositoryImpl
import com.github.yohannestz.popov.data.remote.NetworkService
import com.github.yohannestz.popov.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNotificationCacheDao(notificationDatabase: NotificationDatabase): NotificationCacheDao {
        return notificationDatabase.notificationCacheDao()
    }

    @Provides
    @Singleton
    fun provideRecordCacheDao(notificationDatabase: NotificationDatabase): RecordCacheDao {
        return notificationDatabase.recordCacheDao()
    }

    @Provides
    @Singleton
    fun provideCallLogCacheDao(notificationDatabase: NotificationDatabase): CallLogCacheDao {
        return notificationDatabase.callLogCacheDao()
    }

    @Provides
    @Singleton
    fun provideMessageLogCacheDao(notificationDatabase: NotificationDatabase): MessageLogCacheDao {
        return notificationDatabase.messageCacheDao()
    }

    @Provides
    fun provideRecordRepository(recordCacheDao: RecordCacheDao): RecordRepositoryImpl {
        return RecordRepositoryImpl(recordCacheDao)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(notificationCacheDao: NotificationCacheDao): NotificationRepositoryImpl {
        return NotificationRepositoryImpl(notificationCacheDao)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): NetworkService {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(160, TimeUnit.SECONDS)
            .connectTimeout(160, TimeUnit.SECONDS)
            .writeTimeout(160, TimeUnit.SECONDS)
            .build()


        return Retrofit.Builder()
            .baseUrl(Constants.PHP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(NetworkService::class.java)
    }
}