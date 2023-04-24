package com.dmonster.rewordapp.view.home

import com.dmonster.rewordapp.R
import com.dmonster.rewordapp.base.BaseFragment
import com.dmonster.rewordapp.databinding.FragmentHomeBinding
import com.dmonster.rewordapp.view.dialog.BasicDialog

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        /*val dialog = BasicDialog().setCheckBox(true, getString(R.string.not_showing_week))
            .setTitle(getString(R.string.set_lock_screen))
            .setText(getString(R.string.set_lock_screen_contents))
            .setNegativeButton(true, getString(R.string.cancel)) {

            }.setPositiveButton(true, getString(R.string.set_use)) {

            }.show(childFragmentManager, "")*/
    }

    override fun initViewModelCallback() {

    }
}