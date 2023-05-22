package com.dmonster.dcash.view.newsDetail

import androidx.navigation.fragment.navArgs
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentNewsDetailBinding
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.showSnackBar
import com.dmonster.dcash.utils.webView.ChromeClient
import com.dmonster.dcash.utils.webView.WebClient
import com.dmonster.dcash.utils.webView.WebViewSettingHelper
import com.dmonster.domain.type.NavigateType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
internal class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding, NewsDetailViewModel>() {

    private val helper: WebViewSettingHelper by lazy {
        WebViewSettingHelper(
            WebClient(requireContext(), childFragmentManager),
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
            //mainViewModel.fragmentNavigateTo(NavigateType.BasicDialog())
        }.observeInLifecycleStop(viewLifecycleOwner)
    }

    override fun initViewModelCallback() {

    }
}