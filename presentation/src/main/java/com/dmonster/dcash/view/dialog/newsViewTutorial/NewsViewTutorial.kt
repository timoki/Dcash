package com.dmonster.dcash.view.dialog.newsViewTutorial

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dmonster.dcash.base.BaseBottomSheetDialogFragment
import com.dmonster.dcash.databinding.DialogNewsViewTutorialBinding
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NewsViewTutorial :
    BaseBottomSheetDialogFragment<DialogNewsViewTutorialBinding, NewsViewTutorialViewModel>() {

    private val args: NewsViewTutorialArgs by navArgs()

    override fun init() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.point = args.point

        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun initViewModelCallback(): Unit = viewModel.run {
        onClickChannel.onEach {
            findNavController().popBackStack()
        }.observeInLifecycleStop(viewLifecycleOwner)
    }
}