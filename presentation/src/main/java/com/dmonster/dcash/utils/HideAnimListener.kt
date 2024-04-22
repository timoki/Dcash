package com.dmonster.dcash.utils

import android.view.View
import android.view.animation.Animation

class HideAnimListener(
    private val view: View
) : Animation.AnimationListener {
    override fun onAnimationStart(p0: Animation?) {
        
    }

    override fun onAnimationEnd(p0: Animation?) {
        view.visibility = View.GONE
    }

    override fun onAnimationRepeat(p0: Animation?) {
        
    }

}