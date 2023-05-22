package com.dmonster.dcash.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import com.dmonster.dcash.R
import com.dmonster.dcash.view.dialog.basic.BasicDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

var dialog: BasicDialog? = null

var snackBar: Snackbar? = null

fun showSnackBar(
    activity: Activity,
    message: String
) {
    hideSnackBar()
    val view = activity.findViewById<CoordinatorLayout>(R.id.coordinator)
    val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
    snackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).apply {
        if (fab.isVisible) anchorView = fab
        show()
    }
}

fun hideSnackBar() {
    if (snackBar != null) snackBar!!.dismiss()
}

// 상태바 투명하게 바꿔주는 함수
fun setTransparentStatusBar(
    window: Window,
    rootView: View? = null,
    isLightStatusBar: Boolean = true
) {
    window.apply {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            decorView.systemUiVisibility = when {
                !isLightStatusBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                }

                !isLightStatusBar && Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> {
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }

                else -> View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        } else {
            setDecorFitsSystemWindows(!isLightStatusBar)
            if (isLightStatusBar) {
                // 상태바 내부 Contents Color 초기화
                decorView.windowInsetsController?.setSystemBarsAppearance(
                    0,
                    APPEARANCE_LIGHT_STATUS_BARS
                )
                rootView?.setPadding(0, 0, 0, getNavigationBarHeight(context))
            } else {
                // 상태바 내부 Contents Color 세팅
                decorView.windowInsetsController?.setSystemBarsAppearance(
                    APPEARANCE_LIGHT_STATUS_BARS,
                    APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        }

        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = if (isLightStatusBar) {
            Color.TRANSPARENT
        } else {
            Color.WHITE
        }
    }
}

// 시스템의 Navigation Bar 높이를 가져오는 함수
fun getNavigationBarHeight(context: Context): Int {
    val resources = context.resources
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        return resources.getDimensionPixelSize(resourceId)
    }
    return 0
}

fun Cache.clearMalformedUrls(): Cache {
    // corrupt 된 캐시 삭제하기
    val urlIterator = urls()
    while (urlIterator.hasNext()) {
        if (urlIterator.next().toHttpUrlOrNull() == null) {
            urlIterator.remove()
        }
    }

    return this
}