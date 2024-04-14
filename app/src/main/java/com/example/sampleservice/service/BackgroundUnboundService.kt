package com.example.sampleservice.service

import android.app.Service
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService

class BackgroundUnboundService : LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        // Your background service logic goes here

        Thread {
            while (true) {
                Log.d("BackgroundUnBoundService", "Running...")
                Thread.sleep(2000)
            }
        }.start()

        return Service.START_STICKY
    }
}
