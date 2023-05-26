package com.dmonster.dcash.utils.webView

import android.content.Context
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentManager

class WebClient : WebViewClient() {

    private var helper: WebViewSettingHelper? = null

    fun init(helper: WebViewSettingHelper): WebClient {
        this.helper = helper
        return this
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        helper?.showDialog()
        Log.d(
            "아외안되",
            "shouldOverrideUrlLoading(view : $view, request : $request), requestUrl: ${request?.url.toString()}"
        )
        return true
    }
}