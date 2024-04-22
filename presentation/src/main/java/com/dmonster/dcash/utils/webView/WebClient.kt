package com.dmonster.dcash.utils.webView

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class WebClient : WebViewClient() {

    private var helper: WebViewSettingHelper? = null

    fun init(helper: WebViewSettingHelper): WebClient {
        this.helper = helper
        return this
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        helper?.showDialog(request?.url)

        return true
    }
}