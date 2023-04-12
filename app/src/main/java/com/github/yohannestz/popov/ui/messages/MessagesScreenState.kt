package com.github.yohannestz.popov.ui.messages

import com.github.yohannestz.popov.data.model.Sms

data class MessagesScreenState(
    val isLoading: Boolean = true,
    val messagesList: List<Sms> = emptyList()
)
