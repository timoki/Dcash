package com.dmonster.rewordapp.view.news

import com.dmonster.rewordapp.base.BaseFragment
import com.dmonster.rewordapp.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>() {
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initViewModelCallback() {

    }
}