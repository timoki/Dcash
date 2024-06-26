package com.dmonster.dcash.view.intro

import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentIntroBinding
import com.dmonster.dcash.utils.PermissionViewModel
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.observeOnLifecycleStop
import com.dmonster.dcash.view.dialog.basic.BasicDialog
import com.dmonster.dcash.view.dialog.basic.BasicDialogModel
import kotlinx.coroutines.flow.onEach

class IntroFragment : BaseFragment<FragmentIntroBinding, IntroViewModel>() {

    private var timerFinished = false
    private var checkPermission = false

    private val permissionViewModel: PermissionViewModel by activityViewModels()

    private val timer: CountDownTimer = object : CountDownTimer(2000, 1000) {
        override fun onTick(p0: Long) {

        }

        override fun onFinish() {
            timerFinished = true
            goNextPage()
        }
    }

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        timer.start()

        viewModel.requestGetConfigData()
        mainViewModel.checkPermission(PermissionViewModel.TYPE_INTRO)

        initPermissionViewModelCallback()
    }

    override fun initViewModelCallback() {

    }

    private fun initPermissionViewModelCallback() = with(permissionViewModel) {
        isGrantedPermission.observeOnLifecycleStop(viewLifecycleOwner) {
            checkPermission = true
            goNextPage()
        }

        showPermissionPopup.onEach {
            if (it) {
                findNavController().safeNavigate(IntroFragmentDirections.actionGlobalBasicDialog(
                    BasicDialogModel(titleText = getString(R.string.set_permission_title),
                        text = String.format(
                            getString(R.string.set_permission_contents),
                            getString(R.string.app_name)
                        ),
                        setCancelable = false,
                        setPositiveButton = true to getString(R.string.set_lock_screen_button),
                        buttonClickListener = object : BasicDialog.ButtonClickListener {
                            override fun onPositiveButtonClick(
                                view: View, dialog: BasicDialog
                            ) {
                                super.onPositiveButtonClick(view, dialog)
                                requestPermission(requireActivity())
                            }
                        })))
            }
        }.observeInLifecycleStop(viewLifecycleOwner)

        showSettingPopup.onEach {
            findNavController().safeNavigate(IntroFragmentDirections.actionGlobalBasicDialog(
                BasicDialogModel(titleText = getString(R.string.set_permission_title),
                    text = String.format(
                        getString(R.string.require_permission_contents),
                        getString(R.string.app_name)
                    ),
                    setCancelable = false,
                    setNegativeButton = true to getString(R.string.finish),
                    setPositiveButton = true to getString(R.string.require_permission_button),
                    buttonClickListener = object : BasicDialog.ButtonClickListener {
                        override fun onPositiveButtonClick(
                            view: View, dialog: BasicDialog
                        ) {
                            super.onPositiveButtonClick(view, dialog)
                            mainViewModel.goPermissionSetting()
                        }

                        override fun onNegativeButtonClick(view: View, dialog: BasicDialog) {
                            super.onNegativeButtonClick(view, dialog)
                            requireActivity().finish()
                        }
                    })))
        }.observeInLifecycleStop(viewLifecycleOwner)
    }

    private fun goNextPage() {
        if (timerFinished && checkPermission) {
            findNavController().safeNavigate(
                IntroFragmentDirections.actionIntroFragmentToLoginFragment()
            )
        }
    }
}