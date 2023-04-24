package com.dmonster.rewordapp.view.event

import com.dmonster.rewordapp.base.BaseFragment
import com.dmonster.rewordapp.databinding.FragmentEventBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFragment : BaseFragment<FragmentEventBinding, EventViewModel>() {
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initViewModelCallback() {
        
    }
}