package com.github.yohannestz.popov.data.local.db.impl

import com.github.yohannestz.popov.data.local.db.dao.NotificationCacheDao
import com.github.yohannestz.popov.data.model.Notification
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationCacheDao: NotificationCacheDao
) {
    suspend fun insertNotificationToDb(notification: Notification) {
        notificationCacheDao.insertNotification(notification)
    }

    suspend fun getAllNotifications(): List<Notification> {
        return notificationCacheDao.getAll()
    }

    suspend fun getAllNotificationsForPackage(packageName: String): List<Notification> {
        return notificationCacheDao.getAllByPackageName(packageName)
    }

}