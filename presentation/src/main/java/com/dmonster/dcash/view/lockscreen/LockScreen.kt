package com.dmonster.dcash.view.lockscreen

import android.animation.ValueAnimator
import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dmonster.dcash.R
import com.dmonster.dcash.databinding.ActivityLockScreenBinding
import com.dmonster.dcash.utils.lockscreen.OnSystemKeyPressedListener
import com.dmonster.dcash.utils.lockscreen.SystemButtonWatcher
import com.dmonster.dcash.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LockScreen : AppCompatActivity() {

    private val binding: ActivityLockScreenBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_lock_screen)
    }

    private val viewModel: LockScreenViewModel by viewModels()
    private val watcher: SystemButtonWatcher by lazy {
        SystemButtonWatcher(this, object : OnSystemKeyPressedListener {
            override fun onPressed() {
                val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
                manager.moveTaskToFront(taskId, 0)
            }
        })
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
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        initListener()
        initViewModelCallback()
    }

    private fun initListener() = with(binding) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(view: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(view: SeekBar?) {

            }

            override fun onStopTrackingTouch(view: SeekBar?) {
                view?.let {
                    if (it.progress <= 0) {
                        startActivity(
                            Intent(
                                this@LockScreen,
                                MainActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        )

                        finish()
                        return
                    }

                    if (it.progress >= 100) {
                        finish()
                        return
                    }

                    ValueAnimator.ofInt(it.progress, 50).apply {
                        duration = 100L
                        addUpdateListener { animator ->
                            it.progress = animator.animatedValue as Int
                        }

                        start()
                    }
                }
            }
        })

        /*seekBar.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                if (
                    event.x >= seekBar.thumb.bounds.left &&
                    event.x <= seekBar.thumb.bounds.right &&
                    event.y <= seekBar.thumb.bounds.bottom &&
                    event.y >= seekBar.thumb.bounds.top
                ) {
                    Log.d("아외안되", "1 / ${event.action}")
                    false
                }
            }

            Log.d("아외안되", "2 / ${event.action}")
            true
        }*/
    }

    private fun initViewModelCallback() = with(viewModel) {

    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
    }

    override fun onPause() {
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