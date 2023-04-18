package com.github.yohannestz.popov.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.popov.data.local.CallRepository
import com.github.yohannestz.popov.data.local.MessageRepository
import com.github.yohannestz.popov.data.local.db.impl.CallLogRepositoryImpl
import com.github.yohannestz.popov.data.local.db.impl.MessageLogRepositoryImpl
import com.github.yohannestz.popov.data.model.Call
import com.github.yohannestz.popov.ui.calls.CallScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CallsViewModel @Inject constructor(
    private val callRepository: CallRepository,
    private val messageRepository: MessageRepository,
    private val callLogRepositoryImpl: CallLogRepositoryImpl,
    private val messageLogRepositoryImpl: MessageLogRepositoryImpl
): ViewModel() {
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
        }

        CoroutineScope(Dispatchers.IO).launch {
            //initCache()
            initMessageCache()
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
}