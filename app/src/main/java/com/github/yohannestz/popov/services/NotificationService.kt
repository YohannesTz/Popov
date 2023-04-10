package com.github.yohannestz.popov.services

import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.github.yohannestz.popov.data.local.db.NotificationRepositoryImpl
import com.github.yohannestz.popov.data.model.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationService @Inject constructor(
    private val notificationRepository: NotificationRepositoryImpl
): NotificationListenerService() {

    private val job = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + job)

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            serviceScope.launch {
                notificationRepository.insertNotificationToDb(
                    Notification(sbn!!.uid, sbn.packageName, sbn.packageName,
                        sbn.postTime.toString(),
                        sbn.notification.tickerText as String
                    )
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}