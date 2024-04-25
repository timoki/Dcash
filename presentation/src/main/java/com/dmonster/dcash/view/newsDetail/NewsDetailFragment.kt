package com.dmonster.dcash.view.newsDetail

import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dmonster.dcash.NavigationDirections
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentNewsDetailBinding
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.observeOnLifecycleDestroy
import com.dmonster.dcash.utils.webView.ChromeClient
import com.dmonster.dcash.utils.webView.WebClient
import com.dmonster.dcash.utils.webView.WebViewSettingHelper
import com.dmonster.dcash.view.dialog.basic.BasicDialog
import com.dmonster.dcash.view.dialog.basic.BasicDialogModel
import com.dmonster.dcash.view.dialog.getPoint.GetPoint
import com.dmonster.domain.type.TopMenuType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding, NewsDetailViewModel>() {

    private val helper: WebViewSettingHelper by lazy {
        WebViewSettingHelper(
            WebClient(args.model?.link), ChromeClient()
        )
    }
    private val args: NewsDetailFragmentArgs by navArgs()

    private var checkNewsLoadJob: Job? = null
    private val timer = object : CountDownTimer(10000, 1000) {
        override fun onTick(p0: Long) {}

        override fun onFinish() {
            viewModel.isTimeComplete.value = true
        }
    }

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.showLoadingDialog()
        mainViewModel.topButtonVisible.value = false

        if (args.model?.viewed == 1) {
            mainViewModel.setTopRightMenu(TopMenuType.RightMenu.SHARE)
        }

        binding.webView.apply {
            helper.init(this)
            args.model?.link?.let {
                loadUrl(it)
            }

            setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
                viewModel.isLoadNews.value = true
                checkNewsLoadJob?.cancel()
                checkNewsLoadJob = null

                val newsHeight = mContentsHeight - mComputeVerticalScrollExtent - mPaddingOffset
                if (newsHeight * 0.8 <= scrollY) {
                    viewModel.isScrollComplete.value = true
                }
            }
        }

        /**
         * 뉴스 로드 시 뉴스 자체 url 표시가 아닌
         * 뉴스가 삭제 되거나 하면 즉시 redirect 되는 경우가 있다.
         * 이 때 WebClient Class 의 shouldOverrideUrlLoading method 를 통해 showDialog() 를 호출 하는데
         * showDialog() 의 목적은 뉴스 내에서 다른 링크를 이동 할 경우 외부 browser 를 사용 하기 위함 이다.
         * 그래서 즉시 이동 되는 경우에 대한 처리를 위해 1초 delay 후 뉴스 완전 load 상태를 판단 한다.
         * */
        checkNewsLoadJob = CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            viewModel.isLoadNews.value = true
        }
    }

    override fun initViewModelCallback(): Unit = viewModel.run {
        isLoadNews.observeOnLifecycleDestroy(viewLifecycleOwner) {
            viewModel.hideLoadingDialog()

            // 이미 포인트를 획득한 뉴스는 튜토리얼 표시할 필요 없으며 타이머 동작도 필요 없음
            if (args.model?.viewed == 0) {
                timer.start()
                if (it && !viewModel.isNotShowingNewsViewTutorial()) {
                    findNavController().safeNavigate(
                        NewsDetailFragmentDirections.actionNewsDetailFragmentToNewsViewTutorial(
                            args.model?.point ?: 0
                        )
                    )
                }
            }
        }

        isCanGetPoint.observeOnLifecycleDestroy(viewLifecycleOwner) {
            if (it) {
                requestViewNews(args.model?.guid ?: return@observeOnLifecycleDestroy)
            }
        }

        viewNews.observeOnLifecycleDestroy(viewLifecycleOwner) {
            if (it) {
                findNavController().safeNavigate(
                    NewsDetailFragmentDirections.actionNewsDetailFragmentToGetPoint(
                        args.model?.point ?: 0
                    )
                )

                val model = args.model
                model?.viewed = 1

                setFragmentResult(
                    TAG, bundleOf(
                        "item" to model,
                        "position" to args.itemPosition,
                        "adapterType" to args.adapterType,
                    )
                )

                setFragmentResultListener(GetPoint.TAG) { _, result ->
                    if (result.getBoolean("result")) {
                        mainViewModel.setTopRightMenu(TopMenuType.RightMenu.SHARE)
                    }
                }
            }
        }

        helper.showDialog.onEach { uri ->
            Log.d("아외안되", "origin : ${args.model?.link}\nnew : $uri")
            checkNewsLoadJob?.cancel()
            checkNewsLoadJob = null
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

    override fun initMainViewModelCallback(): Unit = mainViewModel.run {
        actionShareChannel.onEach {
            val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT, args.model?.link
                )
                putExtra(
                    Intent.EXTRA_TITLE, resources.getString(R.string.app_name)
                )
            }

            requireActivity().startActivity(
                Intent.createChooser(
                    intent, getString(R.string.str_action_share)
                )
            )
        }.observeInLifecycleStop(viewLifecycleOwner)
    }

    companion object {
        const val TAG = "NewsDetailFragment"
    }
}