package com.dmonster.dcash.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEach
import androidx.core.view.updateLayoutParams
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.airbnb.lottie.LottieAnimationView
import com.dmonster.dcash.R
import com.dmonster.domain.type.TopMenuType
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.max

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

@BindingAdapter("app:font")
fun setFont(
    textView: TextView,
    fontName: String
) {
    textView.typeface = ResourcesCompat.getFont(textView.context, textView.context.retrievingResources(fontName, "font"))
}

@SuppressLint("DiscouragedApi")
fun Context.retrievingResources(
    resourceName: String,
    resourceType: String
): Int {
    return resources.getIdentifier(resourceName, resourceType, packageName)
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

@SuppressLint("UseCompatLoadingForDrawables")
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
                leftMenuLogoIv.load(context.resources.getDrawable(R.drawable.ic_logo, null))
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
                rightMenuIv.load(context.resources.getDrawable(R.drawable.ic_coin, null))
            }

            TopMenuType.RightMenu.SHARE -> {
                rightMenuSubIv.visibility = View.INVISIBLE
                rightMenuIv.load(context.resources.getDrawable(R.drawable.ic_share, null))
            }

            TopMenuType.RightMenu.NOTIFICATION -> {
                rightMenuSubIv.visibility = View.INVISIBLE
                rightMenuIv.load(context.resources.getDrawable(R.drawable.ic_alarm, null))
            }

            TopMenuType.RightMenu.REMOVE -> {
                rightMenuSubIv.visibility = View.INVISIBLE
                rightMenuIv.load(context.resources.getDrawable(R.drawable.sample_icon, null))
            }

            TopMenuType.RightMenu.POINT_AND_SHARE -> {
                rightMenuSubIv.visibility = View.VISIBLE
                rightMenuIv.load(context.resources.getDrawable(R.drawable.ic_share, null))
                rightMenuSubIv.load(context.resources.getDrawable(R.drawable.ic_coin, null))
            }

            TopMenuType.RightMenu.ATTEND_AND_NOTIFICATION -> {
                rightMenuSubIv.visibility = View.VISIBLE
                rightMenuIv.load(context.resources.getDrawable(R.drawable.ic_alarm, null))
                rightMenuSubIv.load(context.resources.getDrawable(R.drawable.ic_attend, null))
            }

            TopMenuType.RightMenu.SEARCH_AND_NOTIFICATION -> {
                rightMenuSubIv.visibility = View.VISIBLE
                rightMenuIv.load(context.resources.getDrawable(R.drawable.ic_alarm, null))
                rightMenuSubIv.load(context.resources.getDrawable(R.drawable.ic_search, null))
            }
        }
    }
}

@BindingAdapter(
    value = [
        "tabInputLayoutHintColor",
        "textInputEditText"
    ], requireAll = false
)
fun tabInputLayoutHintColor(
    view: TextInputLayout,
    @ColorRes color: Int,
    textInputEditText: String
) {
    if (textInputEditText.isEmpty()) {
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

@BindingAdapter(
    value = [
        "setMargin",
        "setLeftMargin",
        "setRightMargin",
        "setStartMargin",
        "setEndMargin",
        "setTopMargin",
        "setBottomMargin",
    ], requireAll = false
)
fun setViewMargin(
    view: View,
    marginAll: Float = 0f,
    marginLeftDimen: Float = 0f,
    marginRightDimen: Float = 0f,
    marginStartDimen: Float = 0f,
    marginEndDimen: Float = 0f,
    marginTopDimen: Float = 0f,
    marginBottomDimen: Float = 0f,
) {
    val realLeftMargin = max(marginLeftDimen, marginStartDimen)
    val realRightMargin = max(marginRightDimen, marginEndDimen)
    view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        if (marginAll > 0f) {
            topMargin = marginAll.toInt()
            bottomMargin = marginAll.toInt()
            leftMargin = marginAll.toInt()
            rightMargin = marginAll.toInt()

            return
        }

        leftMargin = realLeftMargin.toInt()
        rightMargin = realRightMargin.toInt()
        topMargin = marginTopDimen.toInt()
        bottomMargin = marginBottomDimen.toInt()
    }

    view.invalidate()
}

@BindingAdapter(
    value = [
        "visibilityWithAnimation"
    ]
)
fun setVisibilityWithAnimation(
    view: View,
    visible: Boolean
) {
    view.setVisibilityWithAnimation(visible)
}

@BindingAdapter(
    value = [
        "setItemBottomPadding",
        "setItemEndPadding",
    ], requireAll = false
)
fun setItemBottomPadding(
    view: RecyclerView,
    verticalSpaceHeight: Int = 0,
    horizontalSpaceHeight: Int = 0,
) {
    view.addItemDecoration(
        VerticalSpaceItemDecoration(
            verticalSpaceHeight,
            horizontalSpaceHeight
        )
    )
}

class VerticalSpaceItemDecoration(
    private val verticalSpaceHeight: Int = 0,
    private val horizontalSpaceHeight: Int = 0
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) {
            outRect.bottom = verticalSpaceHeight
            outRect.right = horizontalSpaceHeight
        }
    }
}

@BindingAdapter(
    value = [
        "errorText",
        "childView"
    ]
)
fun setErrorText(
    view: TextInputLayout,
    errorText: String,
    textInputEditText: TextInputEditText?,
) {
    if (errorText.isEmpty()) {
        return
    }

    view.run {
        error = errorText
        textInputEditText?.let { et ->
            TextViewCompat.setCompoundDrawableTintList(
                et,
                ColorStateList.valueOf(
                    resources.getColor(
                        R.color.text_color_error,
                        null
                    )
                )
            )

            et.requestFocus()
        }

    }
}

@BindingAdapter(
    value = [
        "setTabItemMargin"
    ]
)
fun setTabItemMargin(
    view: TabLayout,
    space: Int,
) {
    val tabStrip = view.getChildAt(0) as ViewGroup

    tabStrip.forEach {
        val params = it.layoutParams as ViewGroup.MarginLayoutParams
        params.rightMargin = space
    }
}