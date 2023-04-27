package com.dmonster.rewordapp.view.lockscreen

import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dmonster.rewordapp.R
import com.dmonster.rewordapp.databinding.ActivityLockScreenBinding
import com.dmonster.rewordapp.lockscreen.OnSystemKeyPressedListener
import com.dmonster.rewordapp.lockscreen.SystemButtonWatcher
import com.dmonster.rewordapp.utils.observeInLifecycleStop
import kotlinx.coroutines.flow.onEach

class LockScreen : AppCompatActivity() {

    private val binding: ActivityLockScreenBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_lock_screen)
    }

    private val viewModel: LockScreenViewModel by viewModels()
    private val watcher: SystemButtonWatcher by lazy {
        SystemButtonWatcher(
            this,
            object : OnSystemKeyPressedListener {
                override fun onPressed() {
                    Log.d("아외안되", "눌림")
                    val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
                    manager.moveTaskToFront(taskId, 0)
                }
            }
        )
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            return
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        watcher.startWatch()

        onBackPressedDispatcher.addCallback(this, callback)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            //setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            this.window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }

        initViewModelCallback()
    }

    fun initViewModelCallback() = with(viewModel) {
        lockOffChannel.onEach {
            finish()
        }.observeInLifecycleStop(this@LockScreen)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Log.d("아외안되", "onUserLeaveHint")
    }

    override fun onPause() {
        Log.d("아외안되", "onPause")
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        manager.moveTaskToFront(taskId, 0)
        taskId
        super.onPause()
    }

    override fun onDestroy() {
        watcher.stopWatch()
        super.onDestroy()
    }
}