package com.dmonster.dcash.view.point

import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentPointBinding
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