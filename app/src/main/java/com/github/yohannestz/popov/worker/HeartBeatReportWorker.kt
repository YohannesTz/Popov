package com.github.yohannestz.popov.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.yohannestz.popov.data.model.Bot
import com.github.yohannestz.popov.data.remote.NetworkService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar
import javax.inject.Inject

@HiltWorker
class HeartBeatReportWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val bot: Bot,
    private val networkService: NetworkService
) : CoroutineWorker(context, workerParameters) {

/*    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var bot: Bot*/

    override suspend fun doWork(): Result {
        val now = Calendar.getInstance()
        return try {
            val response = networkService.sendHeartBeat(bot.botId, now.timeInMillis.toString())
            Log.e("success:", response.body().toString())
            if (response.isSuccessful) {
                return Result.success()
            }
            Result.failure()
        } catch (ex:Exception) {
            ex.printStackTrace()
            Result.failure()
        }
    }
}