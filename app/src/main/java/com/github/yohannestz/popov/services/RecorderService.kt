package com.github.yohannestz.popov.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

class RecorderService: Service() {
    private lateinit var phoneCallStateReceiver: PhoneCallStateReceiver

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val phoneStateFilter = IntentFilter("android.intent.action.PHONE_STATE")
        phoneCallStateReceiver = PhoneCallStateReceiver()
        registerReceiver(phoneCallStateReceiver, phoneStateFilter)
        isRunning = true
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(phoneCallStateReceiver)
        isRunning = false
    }

    companion object {
        private var isRunning = false
        fun isRunning() = isRunning
    }
}