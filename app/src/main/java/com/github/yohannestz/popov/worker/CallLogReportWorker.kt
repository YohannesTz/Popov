package com.github.yohannestz.popov.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.yohannestz.popov.data.local.CallRepository
import com.github.yohannestz.popov.data.model.Bot
import com.github.yohannestz.popov.data.model.SendCallsRequestBody
import com.github.yohannestz.popov.data.remote.NetworkService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import javax.inject.Inject

@HiltWorker
class CallLogReportWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val callRepository: CallRepository,
    private val networkService: NetworkService,
    private val bot: Bot
) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        try {
            val callList = callRepository.readCallsForToday()
            val callsRequestBody = SendCallsRequestBody(callList)
            Log.e("body: ", callsRequestBody.toString())
            val response = networkService.sendCalls(bot.botId, callsRequestBody)
            Log.e("success: ", response.body().toString())
            if (response.isSuccessful) {
                return Result.success()
            }
            return Result.retry()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return Result.retry()
        }
    }
}