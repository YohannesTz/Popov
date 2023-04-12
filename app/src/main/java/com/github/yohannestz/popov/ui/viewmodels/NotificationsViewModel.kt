package com.github.yohannestz.popov.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.popov.data.local.db.NotificationRepositoryImpl
import com.github.yohannestz.popov.ui.notifications.NotificationScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationsRepositoryImpl: NotificationRepositoryImpl
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
}