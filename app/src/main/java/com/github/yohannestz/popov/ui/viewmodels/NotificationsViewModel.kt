package com.github.yohannestz.popov.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.popov.data.local.db.NotificationRepositoryImpl
import com.github.yohannestz.popov.data.local.db.RecordRepositoryImpl
import com.github.yohannestz.popov.data.remote.NetworkService
import com.github.yohannestz.popov.ui.notifications.NotificationScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationsRepositoryImpl: NotificationRepositoryImpl,
    private val recordRepositoryImpl: RecordRepositoryImpl,
    private val networkService: NetworkService
): ViewModel() {
    private val _state = mutableStateOf(NotificationScreenState())
    val state: State<NotificationScreenState> = _state

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    isLoading = true
                )
            }

            withContext(Dispatchers.Main) {
                val list = notificationsRepositoryImpl.getAllNotifications()
                Log.e("listSize", list.size.toString())
                _state.value = state.value.copy(
                    notificationList = list,
                    isLoading = false
                )
            }
        }
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
                val uploadList = recordRepositoryImpl.getAllUnUploaded()
                uploadList.map { record ->
                    val file = File(record.fileName)
                    Log.e("fileExist: ", file.exists().toString())
                    val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
                    val fileToUpload = MultipartBody.Part.createFormData("sampleFile", file.name, requestBody)
                    val filename = RequestBody.create(MediaType.parse("multipart/form-data"), file.name)

                    val response = networkService.uploadFile(fileToUpload, filename).body()
                    Log.e("response: ", response.toString())
                    if (response!!.success) {
                        recordRepositoryImpl.markAsUploaded(uploadList[2].id)
                        Log.e("updatedDb: ", "to uploaded for id ${record.id}")
                    }
                }
            }

            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    isLoading = false
                )
            }
        }
    }
}