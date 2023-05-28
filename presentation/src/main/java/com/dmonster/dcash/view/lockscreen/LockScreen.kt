package com.dmonster.dcash.view.lockscreen

import android.animation.ValueAnimator
import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.view.WindowManager
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseActivity
import com.dmonster.dcash.databinding.ActivityLockScreenBinding
import com.dmonster.dcash.utils.lockscreen.OnSystemKeyPressedListener
import com.dmonster.dcash.utils.lockscreen.SystemButtonWatcher
import com.dmonster.dcash.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class LockScreen : BaseActivity<ActivityLockScreenBinding, LockScreenViewModel>(
    R.layout.activity_lock_screen
) {
    override val viewModel: LockScreenViewModel by viewModels()

    private val watcher: SystemButtonWatcher by lazy {
        SystemButtonWatcher(this, object : OnSystemKeyPressedListener {
            override fun onPressed() {
                return
                /*val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
                manager.moveTaskToFront(taskId, 0)*/
            }
        })
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            return
        }
    }

    override fun init() {
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
    }

    override fun initListener() = with(binding) {
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
    }

    override fun initViewModelCallback() = with(viewModel) {

    }

    override fun initErrorCallback() {

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