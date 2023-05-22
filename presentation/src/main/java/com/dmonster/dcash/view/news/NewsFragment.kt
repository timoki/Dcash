package com.dmonster.dcash.view.news

import android.view.View
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.base.MyLoadStateAdapter
import com.dmonster.dcash.databinding.FragmentNewsBinding
import com.dmonster.dcash.utils.observeOnLifecycleStop
import com.dmonster.domain.model.NewsModel
import com.dmonster.domain.type.NavigateType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {

    private val adapter: NewsAdapter by lazy {
        NewsAdapter()
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            (binding.listRv.layoutManager as LinearLayoutManager).apply {
                val firstItemPosition = findFirstVisibleItemPosition()
                //mainViewModel.setTopButtonVisible(firstItemPosition >= 2)
            }
        }
    }

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.refreshListener = this
        binding.listRv.let { rv ->
            rv.adapter = adapter.withLoadStateFooter(
                MyLoadStateAdapter {
                    adapter.retry()
                }
            )
            rv.addOnScrollListener(scrollListener)
        }

        adapter.loadStateFlow.observeOnLifecycleStop(viewLifecycleOwner) {
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

                it.refresh is LoadState.NotLoading && adapter.itemCount == 0 -> {
                    //binding.noResult.errorString = getString(R.string.no_history_account)
                    false
                }

                else -> {
                    true
                }
            }
        }
    }

    override fun initListener() {
        adapter.setOnItemClickListener(object : NewsAdapter.ItemListener {
            override fun onRootClick(item: NewsModel) {
                mainViewModel.fragmentNavigateTo(NavigateType.NewsDetailFromNews(model = item))
            }
        })
    }

    override fun initViewModelCallback(): Unit = with(viewModel) {
        newsList.observeOnLifecycleStop(viewLifecycleOwner) {
            adapter.submitData(it)
        }
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
        adapter.refresh()
    }
}