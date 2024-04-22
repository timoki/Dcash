package com.dmonster.dcash.view.news

import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.base.MyLoadStateAdapter
import com.dmonster.dcash.databinding.FragmentNewsBinding
import com.dmonster.dcash.utils.ScrollListener
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.observeOnLifecycleStop
import com.dmonster.dcash.view.news.adapter.FilterAdapter
import com.dmonster.dcash.view.news.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {

    private val newsAdapter: NewsAdapter = NewsAdapter { item ->
        findNavController().safeNavigate(
            NewsFragmentDirections.actionNewsFragmentToNewsDetailFragment(
                item
            )
        )
    }

    private val creatorAdapter = FilterAdapter {
        it.name?.let { name ->
            if (it.isChecked) {
                viewModel.selectCreator.add(name)
            } else {
                viewModel.selectCreator.remove(name)
            }
        }
        newsAdapter.refresh()
    }

    private val authorAdapter = FilterAdapter {
        it.name?.let { name ->
            if (it.isChecked) {
                viewModel.selectAuthor.add(name)
            } else {
                viewModel.selectAuthor.remove(name)
            }
        }
        newsAdapter.refresh()
    }

    private val scrollListener: ScrollListener by lazy {
        ScrollListener(
            binding.listRv.layoutManager as LinearLayoutManager, mainViewModel
        )
    }

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.refreshListener = this
        binding.listRv.let { rv ->
            rv.adapter =
                newsAdapter.withLoadStateHeaderAndFooter(MyLoadStateAdapter { newsAdapter.retry() },
                    MyLoadStateAdapter { newsAdapter.retry() })
            rv.addOnScrollListener(scrollListener)
        }

        mainViewModel.newsCategory.value?.let {
            it.forEach { filter ->
                binding.categoryTab.run {
                    addTab(
                        newTab().setText(filter.name).setTag(filter.code)
                    )
                }
            }

            viewModel.selectCategory.value = it[0].code ?: ""
            newsAdapter.refresh()
        }

        binding.creatorRv.adapter = creatorAdapter
        binding.authorRv.adapter = authorAdapter

        creatorAdapter.submitList(mainViewModel.newsCreator.value)
        authorAdapter.submitList(mainViewModel.newsAuthor.value)

        newsAdapter.loadStateFlow.observeOnLifecycleStop(viewLifecycleOwner) {
            setShimmer(false)

            binding.isResult = when {
                it.refresh is LoadState.Loading -> {
                    setShimmer(true)
                    true
                }

                it.refresh is LoadState.Error -> {
                    //binding.noResult.errorString = getString(R.string.no_history_error)
                    false
                }

                it.refresh is LoadState.NotLoading && newsAdapter.itemCount == 0 -> {
                    //binding.noResult.errorString = getString(R.string.no_history_account)
                    false
                }

                else -> {
                    true
                }
            }
        }
    }

    override fun initViewModelCallback(): Unit = with(viewModel) {
        newsList.observeOnLifecycleStop(viewLifecycleOwner) { data ->
            newsAdapter.submitData(data)
        }

        isSelectFilter.observeOnLifecycleStop(viewLifecycleOwner) {
            Log.d("아외안되", "isSelectFilter $it")
        }
    }

    /**
     * 한번에 스크롤을 0 으로 위치 시키지 않고 포지션을 잡는 이유는
     * MainActivity 의 BottomAppBar hideOnScroll 속성 때문에 아래로 스크롤을 할 때 BottomAppBar 가 사라지는데
     * scrollToPosition 을 사용하여 최상단으로 올릴 경우 BottomAppBar가 다시 나타나지 않고
     * smoothScrollToPosition 을 사용하면 현재 아이템 포지션이 높을 경우 위로 올라가는 시간이 한참 걸리게 됨
     * scrollToPosition으로 일정 지점까지 한번에 이동 후 이후 BottomAppBar 표시를 위해(추가로 애니메이션 효과를 위해) 해당 방식 적용
     * position > 30 체크는 만약 현재 위치가 30번째보다 낮을경우 갑자기 밑으로 이동하고 위로 올라가는것을 방지하기 위함
     * @author 강석호
     * */
    override fun initMainViewModelCallback(): Unit = with(mainViewModel) {
        scrollTop.onEach {
            binding.listRv.stopScroll()
            val position =
                (binding.listRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            if (position > 30) binding.listRv.scrollToPosition(30)
            binding.listRv.smoothScrollToPosition(0)
        }.observeInLifecycleStop(viewLifecycleOwner)
    }

    private fun setShimmer(isStart: Boolean) {
        binding.shimmer.apply {
            if (isStart) {
                visibility = View.VISIBLE
                startShimmer()
            } else {
                visibility = View.GONE
                stopShimmer()
            }
        }
    }

    override fun onRefresh() {
        binding.refreshView.isRefreshing = false
        //binding.noResultSwipe.isRefreshing = false
        newsAdapter.refresh()
    }
}