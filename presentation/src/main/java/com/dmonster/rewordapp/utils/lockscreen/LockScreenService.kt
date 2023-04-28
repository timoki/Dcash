package com.dmonster.rewordapp.utils.lockscreen

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.dmonster.rewordapp.R

class LockScreenService: Service() {
    var receiver: ScreenOffReceiver? = null

    override fun onCreate() {
        Log.e("!","oncreate")
        super.onCreate()
        if (receiver == null) {
            receiver = ScreenOffReceiver()
            val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
            registerReceiver(receiver, filter)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent != null) {
            if (receiver == null) {
                receiver = ScreenOffReceiver()
                val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
                registerReceiver(receiver, filter)
            }
        }
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "LockScreenService",
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(String.format(getString(R.string.lock_screen_notification_content_title), getString(R.string.app_name)))
            .setContentText(String.format(getString(R.string.lock_screen_notification_content_text), getString(R.string.app_name)))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
        val notification = builder.build()

        // Foreground Service 시작!
        startForeground(NOTIFICATION_ID, notification)

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        if (receiver != null) {
            unregisterReceiver(receiver)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "LockScreenChannel"
        const val NOTIFICATION_ID = 9999

        fun isRunning(context: Context): Boolean {
            val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager

            @Suppress("DEPRECATION")
            return manager.getRunningServices(Integer.MAX_VALUE)
                .any { it.service.className == LockScreenService::class.java.name }
        }
    }
}