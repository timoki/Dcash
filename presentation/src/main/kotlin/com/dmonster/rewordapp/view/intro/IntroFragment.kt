package com.dmonster.rewordapp.view.intro

import android.os.CountDownTimer
import android.util.Log
import com.dmonster.rewordapp.R
import com.dmonster.rewordapp.base.BaseFragment
import com.dmonster.rewordapp.databinding.FragmentIntroBinding
import kr.timoky.domain.model.navi.NavigateType

class IntroFragment: BaseFragment<FragmentIntroBinding, IntroViewModel>() {

    private val timer: CountDownTimer = object : CountDownTimer(2000, 1000) {
        override fun onTick(p0: Long) {
            Log.d("아외안되", "으아아아아아아")
        }

        override fun onFinish() {
            Log.d("아외안되", "해치웠나?")
            viewModel.startActivity()
        }
    }

    override fun init() {
        timer.start()
    }

    override fun initViewModelCallback() {
        
    }
}