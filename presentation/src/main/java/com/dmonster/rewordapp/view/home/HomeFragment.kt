package com.dmonster.rewordapp.view.home

import com.dmonster.domain.type.NavigateType
import com.dmonster.rewordapp.base.BaseFragment
import com.dmonster.rewordapp.databinding.FragmentHomeBinding
import com.dmonster.rewordapp.utils.PermissionViewModel
import com.dmonster.rewordapp.utils.observeInLifecycleStop
import kotlinx.coroutines.flow.onEach

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

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