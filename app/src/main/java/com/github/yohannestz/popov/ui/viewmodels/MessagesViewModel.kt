package com.github.yohannestz.popov.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.yohannestz.popov.data.local.CallRepository
import com.github.yohannestz.popov.data.local.MessageRepository
import com.github.yohannestz.popov.ui.messages.MessagesScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val messagesRepository: MessageRepository
): ViewModel() {
    private val _state = mutableStateOf(MessagesScreenState())
    val state: State<MessagesScreenState> = _state

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    isLoading = true
                )
            }

            withContext(Dispatchers.Default) {
                val list = messagesRepository.getAllMessagesForToday()
                _state.value = state.value.copy(
                     messagesList = list
                )
            }

            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    isLoading = false
                )
            }
        }
    }
}