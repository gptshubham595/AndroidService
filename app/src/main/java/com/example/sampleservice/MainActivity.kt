package com.example.sampleservice

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.sampleservice.databinding.ActivityMainBinding
import com.example.sampleservice.service.BackgroundBoundService
import com.example.sampleservice.service.BackgroundUnboundService
import com.example.sampleservice.service.ForegroundBoundService
import com.example.sampleservice.service.ForegroundUnBoundService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isBound = false


    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            when (service) {
                is ForegroundBoundService.LocalForegroundBinder -> {
                    // Start the foreground service when the client is connected
                    service.service?.startForegroundService()
                }

                is BackgroundBoundService.LocalBackgroundBinder -> {
                    service.service?.startService(
                        Intent(
                            this@MainActivity,
                            BackgroundBoundService::class.java
                        )
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Start the foreground Unbound service
        val foregroundUnboundServiceIntent = Intent(this, ForegroundUnBoundService::class.java)
        ContextCompat.startForegroundService(this, foregroundUnboundServiceIntent)

        // Bind to the foreground bound service
        val foregroundBoundServiceIntent = Intent(this, ForegroundBoundService::class.java)
        bindService(foregroundBoundServiceIntent, serviceConnection, BIND_AUTO_CREATE)

        // Start the background unbound service
        val backgroundUnboundServiceIntent = Intent(this, BackgroundUnboundService::class.java)
        startService(backgroundUnboundServiceIntent)

        binding.btn.setOnClickListener {
            val intent = Intent(this@MainActivity, BActivity::class.java)
            startActivity(intent)
        }
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
