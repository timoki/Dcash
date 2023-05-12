package com.dmonster.dcash.view.login

import android.util.Log
import com.dmonster.domain.type.NavigateType
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentLoginBinding
import com.dmonster.dcash.utils.StaticData
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.observeOnLifecycleStop
import com.dmonster.domain.model.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initViewModelCallback() : Unit = with(viewModel) {
        loginSuccessChannel.onEach {
            mainViewModel.getMemberInfo().observeOnLifecycleStop(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        Log.d("아외안되", "Loading")
                    }

                    is Result.Success -> {
                        Log.d("아외안되", "Success / ${result.data}")
                        mainViewModel.userInfo.value = result.data
                        mainViewModel.fragmentNavigateTo(NavigateType.Home())
                    }

                    is Result.Error -> {
                        Log.d("아외안되", "Error / ${result.message}")
                    }

                    is Result.NetworkError -> {
                        Log.d("아외안되", "NetworkError / ${result.message}")
                    }
                }
            }
        }.observeInLifecycleStop(viewLifecycleOwner)
    }
}