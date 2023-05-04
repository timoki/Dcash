package com.dmonster.dcash.view.login

import com.dmonster.domain.type.NavigateType
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentLoginBinding
import com.dmonster.dcash.utils.observeInLifecycleStop
import kotlinx.coroutines.flow.onEach

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initViewModelCallback() : Unit = with(viewModel) {
        loginClickChannel.onEach {
            mainViewModel.fragmentNavigateTo(NavigateType.Home())
        }.observeInLifecycleStop(viewLifecycleOwner)
    }
}