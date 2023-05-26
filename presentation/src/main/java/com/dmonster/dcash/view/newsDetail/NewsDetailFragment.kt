package com.dmonster.dcash.view.newsDetail

import android.view.View
import androidx.navigation.fragment.navArgs
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentNewsDetailBinding
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.showSnackBar
import com.dmonster.dcash.utils.webView.ChromeClient
import com.dmonster.dcash.utils.webView.WebClient
import com.dmonster.dcash.utils.webView.WebViewSettingHelper
import com.dmonster.dcash.view.dialog.basic.BasicDialog
import com.dmonster.dcash.view.dialog.basic.BasicDialogModel
import com.dmonster.domain.type.NavigateType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
internal class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding, NewsDetailViewModel>() {

    private val helper: WebViewSettingHelper by lazy {
        WebViewSettingHelper(
            WebClient(),
            ChromeClient()
        )
    }
    private val args: NewsDetailFragmentArgs by navArgs()

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.webView.apply {
            helper.init(this)
            args.model?.link?.let {
                loadUrl(it)
            }

            setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
                showSnackBar(
                    requireActivity(),
                    "현재 위치 : $scrollY / 전체 크기 : ${mContentsHeight - mComputeVerticalScrollExtent - mPaddingOffset}"
                )
            }
        }

        helper.showDialog.onEach {
            mainViewModel.fragmentNavigateTo(
                NavigateType.BasicDialog(
                    BasicDialogModel(
                        titleText = getString(R.string.use_external_browser),
                        text = getString(R.string.go_external_browser),
                        setNegativeButton = true to getString(R.string.cancel),
                        setPositiveButton = true to getString(R.string.str_go),
                        buttonClickListener = object : BasicDialog.ButtonClickListener {
                            override fun onPositiveButtonClick(view: View, dialog: BasicDialog) {
                                super.onPositiveButtonClick(view, dialog)

                                // 외부 브라우저 이동
                            }
                        }
                    )
                )
            )
        }.observeInLifecycleStop(viewLifecycleOwner)
    }

    override fun initViewModelCallback() {

    }
}