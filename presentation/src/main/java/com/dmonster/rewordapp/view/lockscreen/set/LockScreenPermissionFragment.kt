package com.dmonster.rewordapp.view.lockscreen.set

import com.dmonster.rewordapp.R
import com.dmonster.rewordapp.base.BaseFragment
import com.dmonster.rewordapp.databinding.FragmentSetLockScreenBinding
import com.dmonster.rewordapp.utils.observeInLifecycleStop
import kotlinx.coroutines.flow.onEach

class LockScreenPermissionFragment :
    BaseFragment<FragmentSetLockScreenBinding, LockScreenPermissionViewModel>() {
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.textView2.text = String.format(
            resources.getString(R.string.set_lock_screen_subject),
            resources.getString(R.string.app_name)
        )
    }

    override fun initViewModelCallback(): Unit = with(viewModel) {
        onClickChannel.onEach {
            mainViewModel.setOverlayPermission()
        }.observeInLifecycleStop(viewLifecycleOwner)
    }
}