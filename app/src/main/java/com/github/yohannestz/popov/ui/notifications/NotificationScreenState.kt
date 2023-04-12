package com.github.yohannestz.popov.ui.notifications

import com.github.yohannestz.popov.data.model.Notification

data class NotificationScreenState(
    val isLoading: Boolean = true,
    val notificationList: List<Notification> = emptyList()
)
