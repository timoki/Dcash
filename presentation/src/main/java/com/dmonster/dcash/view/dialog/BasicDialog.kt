package com.dmonster.dcash.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.dmonster.dcash.R
import com.dmonster.dcash.databinding.DialogBasicBinding

class BasicDialog(
    private val context: Context,
    root: ViewGroup? = null
) {
    private val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)

    private val binding: DialogBasicBinding by lazy {
        DialogBasicBinding.inflate(LayoutInflater.from(context), root, false)
    }

    private var dialog: AlertDialog? = null

    fun setTitle(titleText: String): BasicDialog {
        binding.titleText = titleText
        return this
    }

    fun setText(text: String): BasicDialog {
        binding.text = text
        return this
    }

    fun setCheckBoxVisible(bool: Boolean): BasicDialog {
        return setCheckBox(bool = bool)
    }

    fun setCheckBoxText(text: String): BasicDialog {
        return setCheckBox(text = text)
    }

    fun setCheckBox(bool: Boolean = false, text: String = ""): BasicDialog {
        binding.isCheckBoxVisible = bool
        binding.checkBoxText = text
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

    fun setNegativeButton(bool: Boolean = false, text: String = "", event: ((view: View, dialog: Dialog?) -> Unit)? = null): BasicDialog {
        binding.isNegativeButtonVisible = bool
        binding.negativeButtonText = text
        binding.negativeButtonClickListener =
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

    fun setPositiveButton(bool: Boolean = false, text: String = "", event: ((view: View, dialog: Dialog?) -> Unit)? = null): BasicDialog {
        binding.isPositiveButtonVisible = bool
        binding.positiveButtonText = text
        binding.positiveButtonClickListener =
            View.OnClickListener { v ->
                event?.let {
                    event.invoke(v, dialog)
                } ?: kotlin.run {
                    dialog?.dismiss()
                }
            }
        return this
    }

    fun setCancelable(cancelable: Boolean): BasicDialog {
        builder.setCancelable(cancelable)
        return this
    }

    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): BasicDialog {
        builder.setOnCancelListener(onCancelListener)
        return this
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): BasicDialog {
        builder.setOnDismissListener(onDismissListener)
        return this
    }

    fun create(): AlertDialog {
        builder.setView(binding.root)
        dialog = builder.create()

        return dialog!!
    }

    fun show() {
        create().show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}