package com.github.yohannestz.popov.worker

import androidx.work.DelegatingWorkerFactory
import com.github.yohannestz.popov.data.local.ApplicationsRepository
import com.github.yohannestz.popov.data.local.CallRepository
import com.github.yohannestz.popov.data.local.ContactsRepository
import com.github.yohannestz.popov.data.local.DeviceInfoRepository
import com.github.yohannestz.popov.data.local.MessageRepository
import com.github.yohannestz.popov.data.model.Bot
import com.github.yohannestz.popov.data.remote.NetworkService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PopovDelegatingWorkerFactory @Inject constructor(
    messageRepository: MessageRepository,
    contactsRepository: ContactsRepository,
    callRepository: CallRepository,
    deviceInfoRepository: DeviceInfoRepository,
    applicationsRepository: ApplicationsRepository,
    networkService: NetworkService,
    bot: Bot
): DelegatingWorkerFactory() {
    init {
        addFactory(PopovWorkerFactory(messageRepository, contactsRepository, callRepository, deviceInfoRepository, applicationsRepository, networkService, bot))
    }
}