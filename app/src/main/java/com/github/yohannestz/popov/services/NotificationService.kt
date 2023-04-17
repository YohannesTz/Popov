package com.github.yohannestz.popov.services

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.github.yohannestz.popov.data.local.db.impl.NotificationRepositoryImpl
import com.github.yohannestz.popov.data.model.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


class NotificationService : NotificationListenerService() {

    @Inject
    lateinit var notificationRepository: NotificationRepositoryImpl

    private val job = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + job)

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        isRunning = true
        val extras = sbn!!.notification.extras
        val title = extras.getString("android.title").toString();
        val text = extras.getCharSequence("android.text").toString();
        val notification = Notification(
            sbn.id, title, text,
            sbn.postTime.toString(),
            sbn.notification.tickerText as String
        )

        Log.e("service", "notification logging...")

        serviceScope.launch {
            notificationRepository.insertNotificationToDb(
                notification
            )
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        isRunning = false
    }
    companion object {
        private var isRunning = false
        fun isServiceRunning(): Boolean {
            return isRunning
        }
    }
}