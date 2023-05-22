package com.dmonster.dcash.utils.webView

import android.content.Context
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentManager
import com.dmonster.dcash.R
import com.dmonster.dcash.utils.dialog

class WebClient(
    private val context: Context,
    private val fragmentManager: FragmentManager
) : WebViewClient() {

    private var helper: WebViewSettingHelper? = null

    fun init(helper: WebViewSettingHelper): WebClient {
        this.helper = helper
        return this
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        helper?.showDialog()
        /*dialog?.let { dialog ->
            dialog.setFragmentManager(fragmentManager).setCancel(true)
                .setTitle(context.getString(R.string.use_external_browser))
                .setText(context.getString(R.string.go_external_browser))
                .setPositiveButton(true, context.getString(R.string.str_go)) { _, _ ->
                    dialog.dismiss()
                }.setNegativeButton(true, context.getString(R.string.cancel)) { _, _ ->
                    dialog.dismiss()
                }.setOnDismissListener {
                    fragmentManager.beginTransaction().remove(dialog)
                }.show()
        }*/
        Log.d(
            "아외안되",
            "shouldOverrideUrlLoading(view : $view, request : $request), requestUrl: ${request?.url.toString()}"
        )
        return true
    }
}