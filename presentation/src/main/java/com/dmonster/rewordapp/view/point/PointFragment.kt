package com.dmonster.rewordapp.view.point

import com.dmonster.rewordapp.base.BaseFragment
import com.dmonster.rewordapp.databinding.FragmentPointBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointFragment : BaseFragment<FragmentPointBinding, PointViewModel>() {
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initViewModelCallback() {

    }
}