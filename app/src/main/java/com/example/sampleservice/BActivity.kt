package com.example.sampleservice

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleservice.databinding.ActivityBBinding
import com.example.sampleservice.service.BackgroundBoundService
import com.example.sampleservice.service.ForegroundBoundService

class BActivity : AppCompatActivity() {
    private var isBound = false
    private lateinit var binding: ActivityBBinding

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            when (service) {
                is ForegroundBoundService.LocalForegroundBinder -> {
                    // Start the foreground service when the client is connected
                    service.service?.startForegroundService()
                }

                is BackgroundBoundService.LocalBackgroundBinder -> {
                    val backgroundBoundServiceIntent = Intent(
                        this@BActivity,
                        BackgroundBoundService::class.java
                    )
                    service.service?.startService(
                        backgroundBoundServiceIntent
                    )
                }
            }
            isBound = true


        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind to the background bound service
        val backgroundBoundServiceIntent = Intent(this, BackgroundBoundService::class.java)
        bindService(backgroundBoundServiceIntent, serviceConnection, BIND_AUTO_CREATE)

    }

    override fun onDestroy() {
        super.onDestroy()
        // Unbind from the service
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }
}
