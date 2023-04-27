package com.dmonster.rewordapp.lockscreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

class SystemButtonWatcher(
    private val context: Context,
    private val listener: OnSystemKeyPressedListener,
) {
    private val mFilter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
    private val receiver: InnerReceiver

    init {
        receiver = InnerReceiver()
    }

    fun startWatch() {
        context.registerReceiver(receiver, mFilter)
    }

    fun stopWatch() {
        context.unregisterReceiver(receiver)
    }

    inner class InnerReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            intent?.let {
                Log.d("아외안되", "${it.action}")
                listener.onPressed()
            }
        }
    }
}