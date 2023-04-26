package com.github.yohannestz.popov.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.yohannestz.popov.data.local.DeviceInfoRepository
import com.github.yohannestz.popov.data.model.Bot
import com.github.yohannestz.popov.data.model.SendDeviceInfoRequestBody
import com.github.yohannestz.popov.data.remote.NetworkService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class DeviceInfoReportWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val deviceInfoRepository: DeviceInfoRepository,
    private val bot: Bot,
    private val networkService: NetworkService
) : CoroutineWorker(context, workerParameters) {

    /*    @Inject
        lateinit var deviceInfoRepository: DeviceInfoRepository

        @Inject
        lateinit var bot: Bot

        @Inject
        lateinit var networkService: NetworkService*/

    override suspend fun doWork(): Result {
        val deviceInfo = deviceInfoRepository.getDeviceInformation()
        val deviceInfoReqBody = SendDeviceInfoRequestBody(deviceInfo)
        Log.e("body: ", deviceInfoReqBody.toString())
        val response = networkService.sendDeviceInfo(bot.botId, deviceInfo)
        Log.e("success: ", response.body().toString())

        if (response.isSuccessful) {
            return Result.success()
        }
        return Result.retry()
    }
}