package com.dmonster.rewordapp.utils

import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.lottie.LottieAnimationView

@BindingAdapter(
    value = [
        "coilSrc",
        "coilCircularCrop",
    ], requireAll = false
)
fun setImageResource(
    view: ImageView, @DrawableRes drawableRes: Int?, circularCrop: Boolean = false
) {
    if (drawableRes == null) return

    view.load(drawableRes) {
        if (circularCrop) transformations(CircleCropTransformation())
    }
}

@BindingAdapter(
    value = [
        "lottieSrc",
    ]
)
fun setLottieAnimation(
    view: LottieAnimationView,
    @RawRes src: Int?,
) {
    if (src == null) return

    view.setAnimation(src)
    view.loop(true)
    view.playAnimation()
}