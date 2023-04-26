package com.dmonster.rewordapp.view.home

import com.dmonster.rewordapp.base.BaseFragment
import com.dmonster.rewordapp.databinding.FragmentHomeBinding
import com.dmonster.rewordapp.utils.PermissionViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        mainViewModel.checkPermission(PermissionViewModel.TYPE_LOCK_SCREEN)
    }

    override fun initViewModelCallback() {

    }
}