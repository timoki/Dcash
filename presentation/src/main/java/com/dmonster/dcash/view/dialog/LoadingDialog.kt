package com.dmonster.dcash.view.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.dmonster.dcash.R
import com.dmonster.dcash.databinding.DialogLoadingBinding

class LoadingDialog(
    private val context: Context,
    root: ViewGroup? = null
) {
    private val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)

    private val binding: DialogLoadingBinding by lazy {
        DialogLoadingBinding.inflate(LayoutInflater.from(context), root, false)
    }

    private var dialog: AlertDialog? = null
}