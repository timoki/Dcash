package com.dmonster.dcash.view.dialog.newsViewTutorial

import androidx.navigation.fragment.findNavController
import com.dmonster.dcash.base.BaseBottomSheetDialogFragment
import com.dmonster.dcash.databinding.DialogNewsViewTutorialBinding
import com.dmonster.dcash.utils.observeInLifecycleStop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NewsViewTutorial : BaseBottomSheetDialogFragment<DialogNewsViewTutorialBinding, NewsViewTutorialViewModel>() {
    override fun init() {

    }

    override fun initViewModelCallback(): Unit = viewModel.run {
        onClickChannel.onEach {
            findNavController().popBackStack()
        }.observeInLifecycleStop(viewLifecycleOwner)
    }
}