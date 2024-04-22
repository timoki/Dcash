package com.dmonster.dcash.view.login

import androidx.core.widget.TextViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentLoginBinding
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

    override fun initViewModelCallback(): Unit = with(viewModel) {
        autoLoginCheck()

        loginClickChannel.onEach {
            if (loginIdText.value.isEmpty()) {
                loginIdError.value = getString(R.string.error_login_id_empty)

                return@onEach
            }

            if (loginPwText.value.isEmpty()) {
                loginPwError.value = getString(R.string.error_login_pw_empty)

                return@onEach
            }

            login()
        }.observeInLifecycleStop(viewLifecycleOwner)

        loginSuccessChannel.onEach {
            mainViewModel.getMemberInfo().observeOnLifecycleStop(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoadingDialog()
                    }

                    is Result.Success -> {
                        hideLoadingDialog()
                        mainViewModel.userInfo.value = result.data
                        findNavController().safeNavigate(
                            LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        )
                    }

                    is Result.Error -> {
                        hideLoadingDialog()
                    }

                    is Result.NetworkError -> {
                        hideLoadingDialog()
                    }
                }
            }
        }.observeInLifecycleStop(viewLifecycleOwner)
    }

    override fun initListener(): Unit = with(binding) {
        textInputEtId.addTextChangedListener {
            if (!textInputId.error.isNullOrEmpty()) {
                textInputId.error = ""
                textInputId.isErrorEnabled = false
                TextViewCompat.setCompoundDrawableTintList(
                    textInputEtId, null
                )
            }
        }

        textInputEtPw.addTextChangedListener {
            if (!textInputPw.error.isNullOrEmpty()) {
                textInputPw.error = ""
                textInputPw.isErrorEnabled = false
                TextViewCompat.setCompoundDrawableTintList(
                    textInputEtPw, null
                )
            }
        }
    }
}