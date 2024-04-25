package com.dmonster.dcash.view.dialog.getPoint

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dmonster.dcash.base.BaseBottomSheetDialogFragment
import com.dmonster.dcash.databinding.DialogGetPointBinding
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.observeOnLifecycleStop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GetPoint : BaseBottomSheetDialogFragment<DialogGetPointBinding, GetPointViewModel>() {

    private val args: GetPointArgs by navArgs()

    override fun init() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.point = args.point
        isCancelable = false

        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun initViewModelCallback(): Unit = viewModel.run {
        onClickChannel.onEach {
            setFragmentResult(
                TAG, bundleOf("result" to true)
            )
            findNavController().popBackStack()
        }.observeInLifecycleStop(viewLifecycleOwner)

        userPoint.observeOnLifecycleStop(viewLifecycleOwner) {
            binding.totalPoint = it
        }
    }

    companion object {
        const val TAG = "GetPoint"
    }
}