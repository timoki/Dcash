package com.dmonster.dcash.view.dialog.select

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.dmonster.dcash.base.BaseBottomSheetDialogFragment
import com.dmonster.dcash.databinding.DialogSelectListBinding
import com.dmonster.dcash.view.dialog.select.adapter.SelectDialogAdapter

class SelectDialog :
    BaseBottomSheetDialogFragment<DialogSelectListBinding, SelectDialogViewModel>() {

    private val args: SelectDialogArgs by navArgs()

    private val adapter = SelectDialogAdapter {
        setFragmentResult(
            args.requestTag, bundleOf("selectItem" to it)
        )
        dismiss()
    }

    override fun init() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.title = args.title
        binding.rv.adapter = adapter
        adapter.submitList(args.list.toList())
    }

    override fun initViewModelCallback() {

    }

    companion object {
        const val TAG = "SelectDialog"
    }
}