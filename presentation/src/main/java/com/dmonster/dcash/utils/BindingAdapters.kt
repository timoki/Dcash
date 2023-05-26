package com.dmonster.dcash.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.airbnb.lottie.LottieAnimationView
import com.dmonster.domain.type.TopMenuType
import com.dmonster.dcash.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter(
    value = [
        "onSingleClick",
        "interval"
    ], requireAll = false
)
fun onSingleClick(
    view: View,
    listener: View.OnClickListener? = null,
    interval: Long? = null,
) {
    if (listener != null) {
        view.setOnClickListener(object : Listener.OnSingleClickListener(interval ?: 1000L) {
            override fun onSingleClick(v: View?) {
                listener.onClick(v)
            }
        })
    } else {
        view.setOnClickListener(null)
    }
}

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
        "coilSrc",
        "coilCircularCrop",
    ], requireAll = false
)
fun setImageResource(
    view: ImageView, drawable: Drawable?, circularCrop: Boolean = false
) {
    if (drawable == null) return

    view.load(drawable) {
        if (circularCrop) transformations(CircleCropTransformation())
    }
}

@BindingAdapter(
    value = [
        "coilSrc",
        "coilCircularCrop",
        "coilRoundedCorner",
    ], requireAll = false
)
fun setImageResource(
    view: ImageView,
    image: String?,
    circularCrop: Boolean = false,
    coilRoundedCorner: Boolean = false,
) {
    if (image == null) return

    view.load(image) {
        if (circularCrop) transformations(CircleCropTransformation())
        if (coilRoundedCorner) transformations(RoundedCornersTransformation())
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

@BindingAdapter(
    value = [
        "title",
        "leftMenu",
        "rightMenu",
        "leftMenuLogo",
        "leftMenuBack",
        "leftMenuLogoIv",
        "leftMenuBackIv",
        "rightMenuIv",
        "rightMenuSubIv",
        "leftMenuType",
        "middleMenuType",
        "rightMenuType",
    ]
)
fun setTopMenu(
    view: TextView,
    title: String = "",
    leftMenu: ConstraintLayout,
    rightMenu: ConstraintLayout,
    leftMenuLogo: ConstraintLayout,
    leftMenuBack: ConstraintLayout,
    leftMenuLogoIv: ImageView,
    leftMenuBackIv: ImageView,
    rightMenuIv: ImageView,
    rightMenuSubIv: ImageView,
    leftMenuType: Int,
    middleMenuType: Int,
    rightMenuType: Int,
) {
    val context = view.context
    leftMenuType.let {
        when (it) {
            TopMenuType.LeftMenu.NONE -> {
                leftMenu.visibility = View.INVISIBLE

                return@let
            }

            TopMenuType.LeftMenu.BACK -> {
                leftMenu.visibility = View.VISIBLE
                leftMenuLogo.visibility = View.GONE
                leftMenuBack.visibility = View.VISIBLE
                leftMenuBackIv.load(context.resources.getDrawable(R.drawable.arrow_back, null))
            }

            TopMenuType.LeftMenu.LOGO -> {
                leftMenu.visibility = View.VISIBLE
                leftMenuLogo.visibility = View.VISIBLE
                leftMenuBack.visibility = View.GONE
                leftMenuLogoIv.load(context.resources.getDrawable(R.drawable.sample_icon, null))
            }
        }
    }

    middleMenuType.let {
        when (it) {
            TopMenuType.MiddleMenu.NONE -> {
                view.visibility = View.GONE

                return@let
            }

            TopMenuType.MiddleMenu.TITLE -> {
                view.visibility = View.VISIBLE
                view.text = title
            }
        }
    }

    rightMenuType.let {
        if (it == TopMenuType.RightMenu.NONE) {
            rightMenu.visibility = View.INVISIBLE

            return@let
        }

        rightMenu.visibility = View.VISIBLE
        when (it) {
            TopMenuType.RightMenu.POINT -> {
                rightMenuSubIv.visibility = View.INVISIBLE
                rightMenuIv.load(context.resources.getDrawable(R.drawable.sample_icon, null))
            }

            TopMenuType.RightMenu.SHARE -> {
                rightMenuSubIv.visibility = View.INVISIBLE
                rightMenuIv.load(context.resources.getDrawable(R.drawable.sample_icon, null))
            }

            TopMenuType.RightMenu.NOTIFICATION -> {
                rightMenuSubIv.visibility = View.INVISIBLE
                rightMenuIv.load(context.resources.getDrawable(R.drawable.sample_icon, null))
            }

            TopMenuType.RightMenu.REMOVE -> {
                rightMenuSubIv.visibility = View.INVISIBLE
                rightMenuIv.load(context.resources.getDrawable(R.drawable.sample_icon, null))
            }

            TopMenuType.RightMenu.POINT_AND_SHARE -> {
                rightMenuSubIv.visibility = View.VISIBLE
                rightMenuIv.load(context.resources.getDrawable(R.drawable.sample_icon, null))
                rightMenuSubIv.load(context.resources.getDrawable(R.drawable.sample_icon, null))
            }

            TopMenuType.RightMenu.ATTEND_AND_NOTIFICATION -> {
                rightMenuSubIv.visibility = View.VISIBLE
                rightMenuIv.load(context.resources.getDrawable(R.drawable.sample_icon, null))
                rightMenuSubIv.load(context.resources.getDrawable(R.drawable.sample_icon, null))
            }
        }
    }
}

@BindingAdapter(
    value = [
        "tabInputLayoutHintColor",
        "textInputEditText"
    ]
)
fun tabInputLayoutHintColor(
    view: TextInputLayout,
    @ColorRes color: Int,
    textInputEditText: String
) {
    if (textInputEditText.isNullOrEmpty()) {
        view.defaultHintTextColor = ColorStateList.valueOf(
            ContextCompat.getColor(
                view.context,
                R.color.text_color_hint
            )
        )
    } else {
        view.defaultHintTextColor = ColorStateList.valueOf(
            ContextCompat.getColor(
                view.context,
                color
            )
        )
    }

    view.hintTextColor = ColorStateList.valueOf(
        ContextCompat.getColor(
            view.context,
            color
        )
    )
}