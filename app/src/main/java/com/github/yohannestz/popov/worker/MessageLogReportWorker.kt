package com.github.yohannestz.popov.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.yohannestz.popov.data.local.MessageRepository
import com.github.yohannestz.popov.data.model.Bot
import com.github.yohannestz.popov.data.model.SendSmsRequestBody
import com.github.yohannestz.popov.data.remote.NetworkService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class MessageLogReportWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    @Inject
    lateinit var messageRepository: MessageRepository

    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var bot: Bot

    override suspend fun doWork(): Result {
        val messageList = messageRepository.getAllMessagesForToday()
        val messageReqBody = SendSmsRequestBody(messageList)
        Log.e("body: ", messageReqBody.toString())
        val response = networkService.sendSms(bot.botId, messageReqBody)
        Log.e("success: ", response.body().toString())
        if (response.isSuccessful) {
            return Result.success()
        }
        return Result.retry()
    }
}