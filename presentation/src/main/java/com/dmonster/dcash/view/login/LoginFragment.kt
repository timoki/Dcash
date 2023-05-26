package com.dmonster.dcash.view.login

import android.content.res.ColorStateList
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentLoginBinding
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.observeOnLifecycleStop
import com.dmonster.domain.model.Result
import com.dmonster.domain.type.NavigateType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
internal class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initViewModelCallback(): Unit = with(viewModel) {
        loginSuccessChannel.onEach {
            mainViewModel.getMemberInfo().observeOnLifecycleStop(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoadingDialog()
                        Log.d("아외안되", "Loading")
                    }

                    is Result.Success -> {
                        hideLoadingDialog()
                        Log.d("아외안되", "Success / ${result.data}")
                        mainViewModel.userInfo.value = result.data
                        mainViewModel.fragmentNavigateTo(NavigateType.Home())
                    }

                    is Result.Error -> {
                        hideLoadingDialog()
                        Log.d("아외안되", "Error / ${result.message}")
                    }

                    is Result.NetworkError -> {
                        hideLoadingDialog()
                        Log.d("아외안되", "NetworkError / ${result.message}")
                    }
                }
            }
        }.observeInLifecycleStop(viewLifecycleOwner)

        loginIdError.onEach {
            with(binding) {
                textInputId.error = it
                textInputEtId.compoundDrawableTintList =
                    ColorStateList.valueOf(
                        resources.getColor(
                            R.color.text_color_error,
                            null
                        )
                    )

                textInputEtId.requestFocus()
            }
        }.observeInLifecycleStop(viewLifecycleOwner)

        loginPwError.onEach {
            with(binding) {
                textInputPw.error = it
                textInputEtPw.compoundDrawableTintList =
                    ColorStateList.valueOf(
                        resources.getColor(
                            R.color.text_color_error,
                            null
                        )
                    )

                textInputEtPw.requestFocus()
            }
        }.observeInLifecycleStop(viewLifecycleOwner)
    }

    override fun initListener(): Unit = with(binding) {
        textInputEtId.addTextChangedListener {
            if (!textInputId.error.isNullOrEmpty()) {
                textInputId.error = ""
                textInputId.isErrorEnabled = false
                binding.textInputEtId.compoundDrawableTintList = null
            }
        }

        textInputEtPw.addTextChangedListener {
            if (!textInputPw.error.isNullOrEmpty()) {
                textInputPw.error = ""
                textInputPw.isErrorEnabled = false
                binding.textInputEtPw.compoundDrawableTintList = null
            }
        }
    }
}