package com.dmonster.rewordapp.view.login

import com.dmonster.rewordapp.base.BaseFragment
import com.dmonster.rewordapp.databinding.FragmentLoginBinding
import com.dmonster.rewordapp.utils.observeInLifecycleStop
import kotlinx.coroutines.flow.onEach
import kr.timoky.domain.model.navi.NavigateType

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initViewModelCallback() : Unit = with(viewModel) {
        loginClickChannel.onEach {
            mainViewModel.fragmentNavigateTo(NavigateType.Home(), true)
        }.observeInLifecycleStop(viewLifecycleOwner)
    }
}