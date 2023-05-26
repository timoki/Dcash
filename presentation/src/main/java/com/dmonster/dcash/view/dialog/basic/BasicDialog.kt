package com.dmonster.dcash.view.dialog.basic

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dmonster.dcash.base.BaseDialogFragment
import com.dmonster.dcash.databinding.DialogBasicBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasicDialog : BaseDialogFragment<DialogBasicBinding, BasicDialogViewModel>() {

    private val args: BasicDialogArgs by navArgs()

    override fun init() {
        binding.lifecycleOwner = viewLifecycleOwner

        args.model.apply {
            binding.titleText = titleText
            binding.text = text
            binding.isCheckBoxVisible = setCheckBox.first
            binding.checkBoxText = setCheckBox.second
            binding.isNegativeButtonVisible = setNegativeButton.first
            binding.negativeButtonText = setNegativeButton.second
            binding.isPositiveButtonVisible = setPositiveButton.first
            binding.positiveButtonText = setPositiveButton.second
            buttonClickListener?.let { listener ->
                binding.positiveButtonClickListener = View.OnClickListener {
                    listener.onPositiveButtonClick(binding.positive, this@BasicDialog)
                }

                binding.negativeButtonClickListener = View.OnClickListener {
                    listener.onNegativeButtonClick(binding.negative, this@BasicDialog)
                }
            }
        }
    }

    override fun initViewModelCallback() {

    }

    interface ButtonClickListener {
        fun onPositiveButtonClick(view: View, dialog: BasicDialog) {
            dialog.findNavController().popBackStack()
        }

        fun onNegativeButtonClick(view: View, dialog: BasicDialog) {
            dialog.findNavController().popBackStack()
        }
    }
}