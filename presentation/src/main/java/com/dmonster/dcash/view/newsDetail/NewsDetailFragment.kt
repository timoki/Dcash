package com.dmonster.dcash.view.newsDetail

import android.content.Intent
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dmonster.dcash.NavigationDirections
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentNewsDetailBinding
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.observeOnLifecycleStop
import com.dmonster.dcash.utils.showSnackBar
import com.dmonster.dcash.utils.webView.ChromeClient
import com.dmonster.dcash.utils.webView.WebClient
import com.dmonster.dcash.utils.webView.WebViewSettingHelper
import com.dmonster.dcash.view.dialog.basic.BasicDialog
import com.dmonster.dcash.view.dialog.basic.BasicDialogModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding, NewsDetailViewModel>() {

    private val helper: WebViewSettingHelper by lazy {
        WebViewSettingHelper(
            WebClient(), ChromeClient()
        )
    }
    private val args: NewsDetailFragmentArgs by navArgs()

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.showLoadingDialog()

        /**
         * 뉴스 로드 시 뉴스 자체 url 표시가 아닌
         * 뉴스가 삭제 되거나 하면 즉시 redirect 되는 경우가 있다.
         * 이 때 WebClient Class 의 shouldOverrideUrlLoading method 를 통해 showDialog() 를 호출 하는데
         * showDialog() 의 목적은 뉴스 내에서 다른 링크를 이동 할 경우 외부 browser 를 사용 하기 위함 이다.
         * 그래서 즉시 이동 되는 경우에 대한 처리를 위해 2초 delay 후 뉴스 완전 load 상태를 판단 한다.
         * */
        val checkNewsLoadJob = CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            viewModel.isLoadNews.value = true
        }

        binding.webView.apply {
            helper.init(this)
            args.model?.link?.let {
                loadUrl(it)
            }

            setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
                viewModel.isLoadNews.value = true
                checkNewsLoadJob.cancel()

                val newsHeight = mContentsHeight - mComputeVerticalScrollExtent - mPaddingOffset
                showSnackBar(
                    requireActivity(),
                    "현재 위치 : $scrollY / 전체 크기 : ${mContentsHeight - mComputeVerticalScrollExtent - mPaddingOffset}"
                )

                if (newsHeight - scrollY <= 100) {

                }
            }
        }
    }

    override fun initViewModelCallback(): Unit = viewModel.run {
        isLoadNews.observeOnLifecycleStop(viewLifecycleOwner) {
            viewModel.hideLoadingDialog()
            if (it && !viewModel.isNotShowingNewsViewTutorial()) {
                findNavController().safeNavigate(
                    NewsDetailFragmentDirections.actionNewsDetailFragmentToNewsViewTutorial(
                        args.model?.point ?: 0
                    )
                )
            }
        }

        helper.showDialog.onEach { uri ->
            findNavController().safeNavigate(
                NavigationDirections.actionGlobalBasicDialog(
                    if (isLoadNews.value) {
                        BasicDialogModel(titleText = getString(R.string.use_external_browser),
                            text = getString(R.string.go_external_browser),
                            setNegativeButton = true to getString(R.string.cancel),
                            setPositiveButton = true to getString(R.string.str_go),
                            buttonClickListener = object : BasicDialog.ButtonClickListener {
                                override fun onPositiveButtonClick(
                                    view: View, dialog: BasicDialog
                                ) {
                                    super.onPositiveButtonClick(view, dialog)
                                    requireActivity().startActivity(
                                        Intent(Intent.ACTION_VIEW, uri)
                                    )
                                }
                            })
                    } else {
                        BasicDialogModel(titleText = getString(R.string.str_not_load_news_title),
                            text = getString(R.string.str_not_load_news),
                            setPositiveButton = true to getString(R.string.confirm),
                            buttonClickListener = object : BasicDialog.ButtonClickListener {
                                override fun onPositiveButtonClick(
                                    view: View, dialog: BasicDialog
                                ) {
                                    super.onPositiveButtonClick(view, dialog)
                                    findNavController().popBackStack()
                                }
                            })
                    }
                )
            )
        }.observeInLifecycleStop(viewLifecycleOwner)
    }
}