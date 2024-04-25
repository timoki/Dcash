package com.dmonster.dcash.utils.webView

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class WebClient(
    private val firstLink: String?
) : WebViewClient() {

    private var helper: WebViewSettingHelper? = null

    fun init(helper: WebViewSettingHelper): WebClient {
        this.helper = helper
        return this
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        // 단순 http -> https 로 변환되는 경우는 상관 없도록 체크
        val firstLinkSplit = firstLink?.split("//")?.get(1)
        val requestLinkSplit = request?.url.toString().split("//")[1]

        if (!firstLinkSplit.equals(requestLinkSplit)) {
            helper?.showDialog(request?.url)

            return true
        }

        return false
    }
}