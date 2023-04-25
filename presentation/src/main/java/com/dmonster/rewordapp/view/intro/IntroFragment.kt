package com.dmonster.rewordapp.view.intro

import android.os.CountDownTimer
import com.dmonster.rewordapp.base.BaseFragment
import com.dmonster.rewordapp.databinding.FragmentIntroBinding
import com.dmonster.domain.type.NavigateType

class IntroFragment: BaseFragment<FragmentIntroBinding, IntroViewModel>() {

    private val timer: CountDownTimer = object : CountDownTimer(2000, 1000) {
        override fun onTick(p0: Long) {

        }

        override fun onFinish() {
            mainViewModel.fragmentNavigateTo(NavigateType.Login(), true)
        }
    }

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        timer.start()
    }

    override fun initViewModelCallback() {
        
    }
}