package com.dmonster.dcash.utils.webView

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.WebView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WebViewSettingHelper(
    private val webClient: WebClient,
    private val chromeClient: ChromeClient
) {

    private val _showDialog = Channel<Uri?>(Channel.CONFLATED)
    val showDialog = _showDialog.receiveAsFlow()

    @SuppressLint("SetJavaScriptEnabled")
    fun init(view: WebView?) {
        view?.let { webView ->
            webView.webViewClient = webClient.init(this)
            webView.webChromeClient = chromeClient.init(this)
            webView.settings.loadWithOverviewMode = true // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
            webView.settings.useWideViewPort = true // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함
            webView.settings.setSupportZoom(false) // 줌 설정 여부
            webView.settings.builtInZoomControls = false // 줌 확대/축소 버튼 여부
            webView.settings.javaScriptEnabled = true // 자바스크립트 사용여부
            webView.settings.javaScriptCanOpenWindowsAutomatically = true // javascript가 window.open()을 사용할 수 있도록 설정
            webView.settings.setSupportMultipleWindows(true) // 멀티 윈도우 사용 여부
            webView.settings.domStorageEnabled = true // 로컬 스토리지
        }
    }

    fun showDialog(link: Uri?) = CoroutineScope(Dispatchers.Main).launch {
        _showDialog.send(link)
    }
}