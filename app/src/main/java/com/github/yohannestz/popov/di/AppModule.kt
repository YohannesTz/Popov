package com.github.yohannestz.popov.di

import android.app.Application
import android.content.Context
import com.github.yohannestz.popov.data.local.ApplicationsRepository
import com.github.yohannestz.popov.data.local.CallRepository
import com.github.yohannestz.popov.data.local.DeviceInfoRepository
import com.github.yohannestz.popov.data.local.MessageRepository
import com.github.yohannestz.popov.data.model.Bot
import com.github.yohannestz.popov.util.Constants
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
    fun provideBotInstance(): Bot {
        return Bot(Constants.BOT_ID)
    }

    @Provides
    @Singleton
    fun provideAppRepository(context: Context): ApplicationsRepository {
        return ApplicationsRepository(context)
    }

    @Provides
    @Singleton
    fun provideDeviceInfoRepository(): DeviceInfoRepository {
        return DeviceInfoRepository()
    }
}