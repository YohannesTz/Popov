package com.github.yohannestz.popov.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.popov.data.local.CallRepository
import com.github.yohannestz.popov.ui.calls.CallScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CallsViewModel @Inject constructor(
    private val callRepository: CallRepository
): ViewModel() {
    private val _state = mutableStateOf(CallScreenState())
    val state: State<CallScreenState> = _state

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    isLoading = true
                )
            }

            withContext(Dispatchers.Default) {
                val list = callRepository.readAllCalls()
                _state.value = state.value.copy(
                    callLogList = list
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