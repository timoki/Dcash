package com.dmonster.dcash.base

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle

abstract class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes private val layoutResId: Int
) : AppCompatActivity() {
    protected val binding: VB by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView(this, layoutResId)
    }

    init {
        addOnContextAvailableListener { binding.notifyChange() }
    }

    abstract val viewModel: VM

    protected fun checkViewLifeCycleOwnerResumed() =
        lifecycle.currentState == Lifecycle.State.RESUMED

    abstract fun init()
    open fun initListener() {}
    abstract fun initViewModelCallback()
    open fun initNetworkViewModelCallback() {}
    open fun initPermissionViewModelCallback() {}
    open fun initTopViewModelCallback() {}
    abstract fun initErrorCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        init()
        initListener()
        initViewModelCallback()
        initNetworkViewModelCallback()
        initPermissionViewModelCallback()
        initTopViewModelCallback()
        initErrorCallback()
    }
}