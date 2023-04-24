package com.dmonster.rewordapp.view.dialog

import com.dmonster.rewordapp.base.BaseDialogFragment
import com.dmonster.rewordapp.databinding.DialogBasicBinding

class BasicDialog : BaseDialogFragment<DialogBasicBinding, BasicDialogViewModel>() {
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initViewModelCallback() {

    }

    fun setTitle(titleText: String): BasicDialog {
        viewModel.setTitleText(titleText)
        return this
    }

    fun setText(text: String): BasicDialog {
        viewModel.setSubjectText(text)
        return this
    }

    fun setCheckBoxVisible(bool: Boolean): BasicDialog {
        viewModel.setCheckBoxVisible(bool)
        return this
    }

    fun setCheckBoxText(text: String): BasicDialog {
        viewModel.setCheckBoxText(text)
        return this
    }

    fun setCheckBox(bool: Boolean, text: String): BasicDialog {
        viewModel.setCheckBox(bool, text)
        return this
    }

    fun setNegativeButtonVisible(bool: Boolean): BasicDialog {
        viewModel.setNegativeButtonVisible(bool)
        return this
    }

    fun setNegativeButtonText(text: String): BasicDialog {
        viewModel.setNegativeButtonText(text)
        return this
    }

    fun setNegativeButtonClickListener(event: () -> Unit): BasicDialog {
        viewModel.setNegativeButtonClickListener(event)
        return this
    }

    fun setNegativeButton(bool: Boolean, text: String, event: () -> Unit): BasicDialog {
        viewModel.setNegativeButton(
            bool, text, event
        )
        return this
    }

    fun setPositiveButtonVisible(bool: Boolean): BasicDialog {
        viewModel.setPositiveButtonVisible(bool)
        return this
    }

    fun setPositiveButtonText(text: String): BasicDialog {
        viewModel.setPositiveButtonText(text)
        return this
    }

    fun setPositiveButtonClickListener(event: () -> Unit): BasicDialog {
        viewModel.setPositiveButtonClickListener(event)
        return this
    }

    fun setPositiveButton(bool: Boolean, text: String, event: () -> Unit): BasicDialog {
        viewModel.setPositiveButton(
            bool, text, event
        )
        return this
    }
}