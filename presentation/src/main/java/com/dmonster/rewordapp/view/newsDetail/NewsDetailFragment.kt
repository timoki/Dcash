package com.dmonster.rewordapp.view.newsDetail

import android.util.Log
import com.dmonster.rewordapp.base.BaseFragment
import com.dmonster.rewordapp.databinding.FragmentNewsDetailBinding
import com.dmonster.rewordapp.utils.showSnackBar
import com.dmonster.rewordapp.utils.webView.WebViewSettingHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding, NewsDetailViewModel>() {

    private val helper = WebViewSettingHelper()

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.webView.apply {
            helper.init(this)
            loadUrl("http://taptalk.net/news/view/2023041300001022166/GDtaJNph0/")

            setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
                showSnackBar(requireActivity(), "현재 위치 : $scrollY / 전체 크기 : ${mContentsHeight - mComputeVerticalScrollExtent - mPaddingOffset}")
                Log.d("아외안되", "$scrollY / $mContentsHeight / $mComputeVerticalScrollOffset / $mComputeVerticalScrollExtent / $mPaddingOffset")
            }
        }
    }

    override fun initViewModelCallback() {
        
    }
}