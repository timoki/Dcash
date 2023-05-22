package com.dmonster.dcash.utils.webView

import android.webkit.WebChromeClient

class ChromeClient : WebChromeClient() {

    private var helper: WebViewSettingHelper? = null

    fun init(helper: WebViewSettingHelper): ChromeClient {
        this.helper = helper
        return this
    }
}