package com.dmonster.dcash.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.createViewModelLazy
import androidx.viewbinding.ViewBinding
import com.dmonster.dcash.utils.observeOnLifecycleDestroy
import com.dmonster.dcash.view.dialog.LoadingDialog
import com.dmonster.dcash.view.main.MainActivity
import com.dmonster.dcash.view.main.MainViewModel
import java.lang.reflect.ParameterizedType

internal abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {
    abstract fun init()
    open fun initListener() {}
    abstract fun initViewModelCallback()
    open fun navigationBackStackCallback() {}
    open fun initMainViewModelCallback() {}

    lateinit var binding: VB
    protected lateinit var viewModel: VM
    val mainViewModel: MainViewModel by activityViewModels()

    private val type = (javaClass.genericSuperclass as ParameterizedType)
    private val classVB = type.actualTypeArguments[0] as Class<VB>
    private val classVM = type.actualTypeArguments[1] as Class<VM>

    private val inflateMethod = classVB.getMethod(
        "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = inflateMethod.invoke(null, inflater, container, false) as VB
        viewModel = createViewModelLazy(classVM.kotlin, { viewModelStore }).value
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initListener()
        initViewModelCallback()
        initMainViewModelCallback()
        navigationBackStackCallback()

        viewModel.isLoading.observeOnLifecycleDestroy(viewLifecycleOwner) { show ->
            if (show) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        }
    }

    private var loadingDialog: LoadingDialog? = null
    private var loadingCnt: Int = 0

    private fun showLoadingDialog() {
        loadingCnt++

        if (loadingDialog?.isShowing == true) {
            return
        }

        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog?.show()
    }

    private fun hideLoadingDialog() {
        if (loadingCnt >= 1) {
            loadingCnt--

            if (loadingCnt == 0 && loadingDialog != null) {
                loadingDialog?.dismiss()
                loadingDialog = null
            }
        }
    }
}