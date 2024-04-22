package com.dmonster.dcash.view.home

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentHomeBinding
import com.dmonster.dcash.utils.PermissionViewModel
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.observeOnLifecycleStop
import com.dmonster.dcash.view.dialog.basic.BasicDialog
import com.dmonster.dcash.view.dialog.basic.BasicDialogModel
import com.dmonster.dcash.view.home.adapter.HomeAdapter
import com.dmonster.domain.model.home.FilterModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    private val bannerAdapter = HomeAdapter(HomeAdapter.TYPE_BANNER) { item ->
        findNavController().safeNavigate(
            HomeFragmentDirections.actionHomeFragmentToNewsDetailFragment(
                item
            )
        )
    }

    private val recommendHorizontalAdapter = HomeAdapter(HomeAdapter.TYPE_RECOMMEND_BIG) { item ->
        findNavController().safeNavigate(
            HomeFragmentDirections.actionHomeFragmentToNewsDetailFragment(
                item
            )
        )
    }

    private val recommendVerticalAdapter = HomeAdapter(HomeAdapter.TYPE_RECOMMEND) { item ->
        findNavController().safeNavigate(
            HomeFragmentDirections.actionHomeFragmentToNewsDetailFragment(
                item
            )
        )
    }

    private val customAdapter = HomeAdapter(HomeAdapter.TYPE_CUSTOM) { item ->
        findNavController().safeNavigate(
            HomeFragmentDirections.actionHomeFragmentToNewsDetailFragment(
                item
            )
        )
    }

    private val categoryAdapter = HomeAdapter(HomeAdapter.TYPE_CATEGORY) { item ->
        findNavController().safeNavigate(
            HomeFragmentDirections.actionHomeFragmentToNewsDetailFragment(
                item
            )
        )
    }

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        mainViewModel.apply {
            if (!isShowLockScreenPopup) {
                checkPermission(PermissionViewModel.TYPE_LOCK_SCREEN)
                isShowLockScreenPopup = true
            }
        }

        binding.mainBannerNews.offscreenPageLimit = 1
        binding.mainBannerNews.adapter = bannerAdapter
        binding.indicator.attachTo(binding.mainBannerNews)

        binding.recommendNewsRvHorizontal.adapter = recommendHorizontalAdapter
        binding.recommendNewsRvVertical.adapter = recommendVerticalAdapter

        binding.customNewsRv.adapter = customAdapter
        binding.customNewsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (recyclerView.layoutManager as LinearLayoutManager).apply {
                    binding.customCurrentIndex =
                        if (!recyclerView.canScrollHorizontally(1) && dx > 0) {
                            findLastVisibleItemPosition().plus(1).toString()
                        } else {
                            findFirstVisibleItemPosition().plus(1).toString()
                        }
                }
            }
        })

        binding.categoryNews.adapter = categoryAdapter

        binding.swipe.setOnRefreshListener {
            viewModel.getHomeData()
            binding.swipe.isRefreshing = false
        }
    }

    override fun initViewModelCallback(): Unit = viewModel.run {
        getHomeDataErrorChannel.onEach {
            BasicDialogModel(titleText = getString(R.string.str_load_error_title),
                text = it ?: getString(R.string.str_load_home_data_error),
                setCancelable = false,
                setPositiveButton = true to getString(R.string.confirm),
                buttonClickListener = object : BasicDialog.ButtonClickListener {})
        }.observeInLifecycleStop(viewLifecycleOwner)

        homeData.observeOnLifecycleStop(viewLifecycleOwner) {
            it?.let { item ->
                binding.isBannerVisible = !item.banner.isNullOrEmpty()
                bannerAdapter.submitList(item.banner)

                binding.isRecommendVisible = !item.recommend.isNullOrEmpty()
                binding.isRecommendSubVisible =
                    !item.recommend.isNullOrEmpty() && item.recommend!!.size > 2
                item.recommend?.let { recommend ->
                    val horizontalList = recommend.slice(0..1)
                    val verticalList = if (moreRecommend.value) {
                        recommend.slice(2 until recommend.size)
                    } else {
                        recommend.slice(2..4)
                    }

                    recommendHorizontalAdapter.submitList(horizontalList)
                    recommendVerticalAdapter.submitList(verticalList)
                }

                binding.isCustomVisible = !item.custom.isNullOrEmpty()
                customAdapter.submitList(item.custom)
                binding.customTotalCount = item.custom?.size.toString()

                binding.isCategoryVisible = !item.category.isNullOrEmpty()
                setCategoryTab(item.category)
                val selectCategoryNewsList = item.categoryNews?.find { news ->
                    news.ctName?.equals(binding.categoryNewsTab.getTabAt(0)?.tag) ?: false
                }

                categoryAdapter.submitList(selectCategoryNewsList?.news)

                mainViewModel.run {
                    newsCategory.value = item.category
                    newsCreator.value = item.creator
                    newsAuthor.value = item.author
                }
            }
        }

        moreRecommendClickChannel.onEach {
            moreRecommend.value = true
        }.observeInLifecycleStop(viewLifecycleOwner)

        allNewsClickChannel.onEach {
            mainViewModel.onBottomMenuClick(1)
        }.observeInLifecycleStop(viewLifecycleOwner)

        moreRecommend.observeOnLifecycleStop(viewLifecycleOwner) {
            homeData.value?.let { item ->
                item.recommend?.let { recommend ->
                    val verticalList = if (it) {
                        recommend.slice(2 until recommend.size)
                    } else {
                        recommend.slice(2..4)
                    }
                    recommendVerticalAdapter.submitList(verticalList)
                }
            }
        }
    }

    private fun setCategoryTab(list: List<FilterModel>?) {
        binding.categoryNewsTab.apply {
            removeAllTabs()
            addTab(
                newTab().setText(context.getString(R.string.str_populer))
                    .setTag(context.getString(R.string.str_populer))
            )

            addTab(
                newTab().setText(context.getString(R.string.str_all))
                    .setTag(context.getString(R.string.str_all))
            )

            list?.forEach {
                addTab(
                    newTab().setText(it.name).setTag(it.code)
                )
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val selectCategoryNewsList =
                        viewModel.homeData.value?.categoryNews?.find { news ->
                            news.ctName?.equals(tab?.tag) ?: false
                        }

                    categoryAdapter.submitList(selectCategoryNewsList?.news)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
        }
    }
}