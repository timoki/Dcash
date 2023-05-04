package com.dmonster.dcash.customView

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

class MyWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(
    context,
    attrs,
    defStyleAttr
) {
    val mContentsHeight: Int
        get() = super.computeVerticalScrollRange()

    var mComputeVerticalScrollOffset = 0
        get() =
            super.computeVerticalScrollOffset()

    var mComputeVerticalScrollExtent = 0
        get() =
            super.computeVerticalScrollExtent()

    var mPaddingOffset = 0
        get() =
            super.getBottomPaddingOffset() + super.getTopPaddingOffset()
}