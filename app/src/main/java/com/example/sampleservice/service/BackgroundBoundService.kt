package com.example.sampleservice.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService

class BackgroundBoundService : LifecycleService() {
    private val binder: IBinder = LocalBackgroundBinder()

    private val CHANNEL_ID = "BackgroundUnboundServiceChannel"
    private val NOTIFICATION_ID = 3

    inner class LocalBackgroundBinder : Binder() {
        val service: BackgroundBoundService
            get() = this@BackgroundBoundService
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return binder
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        // Your background service logic goes here

        Thread {
            while (true) {
                Log.d("BackgroundBoundService", "Running...")
                Thread.sleep(2000)
            }
        }.start()

        return Service.START_STICKY
    }
}
