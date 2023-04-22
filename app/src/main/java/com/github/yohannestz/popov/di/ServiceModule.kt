package com.github.yohannestz.popov.di

import com.github.yohannestz.popov.data.local.db.dao.NotificationCacheDao
import com.github.yohannestz.popov.data.local.db.impl.NotificationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideNotificationRepository(notificationCacheDao: NotificationCacheDao): NotificationRepositoryImpl {
        return NotificationRepositoryImpl(notificationCacheDao)
    }
}