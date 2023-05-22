package com.dmonster.dcash.view.dialog.basic

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.navArgs
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseDialogFragment
import com.dmonster.dcash.databinding.DialogBasicBinding
import com.dmonster.dcash.view.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasicDialog : BaseDialogFragment<DialogBasicBinding, BasicDialogViewModel>() {

    /*private var fragmentManager: FragmentManager? = null

    private var titleText: String? = null
    private var text: String? = null
    private var isCheckBoxVisible = false
    private var checkBoxText: String? = null
    private var isNegativeButtonVisible = false
    private var negativeButtonText: String? = null
    private var negativeButtonClickListener: View.OnClickListener? = null
    private var isPositiveButtonVisible = false
    private var positiveButtonText: String? = null
    private var positiveButtonClickListener: View.OnClickListener? = null

    fun setFragmentManager(fragmentManager: FragmentManager?): BasicDialog {
        this.fragmentManager = fragmentManager
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.AlertDialogTheme)
    }

    override fun init() {
        if (!titleText.isNullOrEmpty()) binding.titleText = titleText
        if (!text.isNullOrEmpty()) binding.text = text
        binding.isCheckBoxVisible = isCheckBoxVisible
        if (!checkBoxText.isNullOrEmpty()) binding.checkBoxText = checkBoxText
        binding.isNegativeButtonVisible = isNegativeButtonVisible
        if (!negativeButtonText.isNullOrEmpty()) binding.negativeButtonText = negativeButtonText
        if (negativeButtonClickListener != null) binding.negativeButtonClickListener = negativeButtonClickListener
        binding.isPositiveButtonVisible = isPositiveButtonVisible
        if (!positiveButtonText.isNullOrEmpty()) binding.positiveButtonText = positiveButtonText
        if (positiveButtonClickListener != null) binding.positiveButtonClickListener = positiveButtonClickListener
    }

    override fun initViewModelCallback() {

    }

    fun setTitle(titleText: String): BasicDialog {

        this.titleText = titleText
        return this
    }

    fun setText(text: String): BasicDialog {
        this.text = text
        return this
    }

    fun setCheckBoxVisible(bool: Boolean): BasicDialog {
        return setCheckBox(bool = bool)
    }

    fun setCheckBoxText(text: String): BasicDialog {
        return setCheckBox(text = text)
    }

    fun setCheckBox(bool: Boolean = false, text: String = ""): BasicDialog {
        this.isCheckBoxVisible = bool
        this.checkBoxText = text
        return this
    }

    fun setNegativeButtonVisible(bool: Boolean): BasicDialog {
        return setNegativeButton(bool = bool)
    }

    fun setNegativeButtonText(text: String): BasicDialog {
        return setNegativeButton(text = text)
    }

    fun setNegativeButtonClickListener(event: (view: View, dialog: Dialog?) -> Unit): BasicDialog {
        return setNegativeButton(event = event)
    }

    fun setNegativeButton(
        bool: Boolean = false,
        text: String = "",
        event: ((view: View, dialog: Dialog?) -> Unit)? = null
    ): BasicDialog {
        this.isNegativeButtonVisible = bool
        this.negativeButtonText = text
        this.negativeButtonClickListener =
            View.OnClickListener { v ->
                event?.let {
                    event.invoke(v, dialog)
                } ?: kotlin.run {
                    dialog?.dismiss()
                }
            }
        return this
    }

    fun setPositiveButtonVisible(bool: Boolean): BasicDialog {
        return setPositiveButton(bool = bool)
    }

    fun setPositiveButtonText(text: String): BasicDialog {
        return setPositiveButton(text = text)
    }

    fun setPositiveButtonClickListener(event: (view: View, dialog: Dialog?) -> Unit): BasicDialog {
        return setPositiveButton(event = event)
    }

    fun setPositiveButton(
        bool: Boolean = false,
        text: String = "",
        event: ((view: View, dialog: Dialog?) -> Unit)? = null
    ): BasicDialog {
        this.isPositiveButtonVisible = bool
        this.positiveButtonText = text
        this.positiveButtonClickListener =
            View.OnClickListener { v ->
                event?.let {
                    event.invoke(v, dialog)
                } ?: kotlin.run {
                    dialog?.dismiss()
                }
            }
        return this
    }

    fun setCancel(cancelable: Boolean): BasicDialog {
        isCancelable = cancelable
        return this
    }

    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): BasicDialog {
        dialog?.setOnCancelListener(onCancelListener)
        return this
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): BasicDialog {
        dialog?.setOnDismissListener(onDismissListener)
        return this
    }

    fun setOnDismissListener(event: (dialogInterface: DialogInterface) -> Unit): BasicDialog {
        dialog?.setOnDismissListener {
            event.invoke(it)
        }
        return this
    }

    fun show() {
        fragmentManager?.let {
            show(it, "")
        }
    }*/

    private val args: BasicDialogArgs by navArgs()

    override fun init() {
        binding.lifecycleOwner = viewLifecycleOwner

        args.model.apply {
            if (!titleText.isNullOrEmpty()) binding.titleText = titleText
            if (!text.isNullOrEmpty()) binding.text = text
            binding.isCheckBoxVisible = isCheckBoxVisible
            if (!checkBoxText.isNullOrEmpty()) binding.checkBoxText = checkBoxText
            binding.isNegativeButtonVisible = isNegativeButtonVisible
            if (!negativeButtonText.isNullOrEmpty()) binding.negativeButtonText = negativeButtonText
            binding.isPositiveButtonVisible = isPositiveButtonVisible
            if (!positiveButtonText.isNullOrEmpty()) binding.positiveButtonText = positiveButtonText
        }

        args.positiveClickListener?.let {
            binding.positiveButtonClickListener = it
        }

        args.negativeClickListener?.let {
            binding.negativeButtonClickListener = it
        }
    }

    override fun initViewModelCallback() {

    }
}