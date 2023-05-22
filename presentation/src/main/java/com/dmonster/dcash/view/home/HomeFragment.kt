package com.dmonster.dcash.view.home

import com.dmonster.domain.type.NavigateType
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentHomeBinding
import com.dmonster.dcash.utils.PermissionViewModel
import com.dmonster.dcash.utils.observeInLifecycleStop
import kotlinx.coroutines.flow.onEach

internal class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        mainViewModel.apply {
            if (!isShowLockScreenPopup){
                checkPermission(PermissionViewModel.TYPE_LOCK_SCREEN)
                isShowLockScreenPopup = true
            }
        }
    }

    override fun initViewModelCallback(): Unit = with(viewModel) {
        onClickChannel.onEach {
            mainViewModel.fragmentNavigateTo(NavigateType.NewsDetailFromHome())
        }.observeInLifecycleStop(viewLifecycleOwner)
    }
}