package com.dmonster.dcash.view.intro

import android.os.CountDownTimer
import androidx.fragment.app.activityViewModels
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentIntroBinding
import com.dmonster.dcash.utils.PermissionViewModel
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.observeOnLifecycleStop
import com.dmonster.dcash.view.dialog.BasicDialog
import com.dmonster.domain.type.NavigateType
import kotlinx.coroutines.flow.onEach

class IntroFragment: BaseFragment<FragmentIntroBinding, IntroViewModel>() {

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
                BasicDialog(requireActivity())
                    .setCancelable(false)
                    .setTitle(getString(R.string.set_permission_title))
                    .setText(String.format(getString(R.string.set_permission_contents), getString(R.string.app_name)))
                    .setPositiveButton(true, getString(R.string.set_lock_screen_button)) { view, dialog ->
                        dialog?.dismiss()
                        requestPermission(requireActivity())
                    }.show()
            }

            showSettingPopup.onEach {
                BasicDialog(requireActivity())
                    .setCancelable(false)
                    .setTitle(getString(R.string.set_permission_title))
                    .setText(String.format(getString(R.string.require_permission_contents), getString(R.string.app_name)))
                    .setNegativeButton(true, getString(R.string.finish)) { view, dialog ->
                        dialog?.dismiss()
                        requireActivity().finish()
                    }
                    .setPositiveButton(true, getString(R.string.require_permission_button)) { view, dialog ->
                        dialog?.dismiss()
                        mainViewModel.goPermissionSetting()
                    }.show()
            }.observeInLifecycleStop(viewLifecycleOwner)
        }.observeInLifecycleStop(viewLifecycleOwner)
    }

    private fun goNextPage() {
        if (timerFinished && checkPermission) {
            mainViewModel.fragmentNavigateTo(NavigateType.Login())
        }
    }
}