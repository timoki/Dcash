package com.dmonster.dcash.view.point

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.base.MyLoadStateAdapter
import com.dmonster.dcash.databinding.FragmentPointBinding
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.observeOnLifecycleStop
import com.dmonster.dcash.view.dialog.basic.BasicDialog
import com.dmonster.dcash.view.dialog.basic.BasicDialogModel
import com.dmonster.dcash.view.point.adapter.PointAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PointFragment : BaseFragment<FragmentPointBinding, PointViewModel>(),
    SwipeRefreshLayout.OnRefreshListener {

    private val adapter = PointAdapter()

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.refreshListener = this

        viewModel.showLoadingDialog()

        binding.rv.adapter = adapter.withLoadStateFooter(MyLoadStateAdapter { adapter.retry() })

        adapter.loadStateFlow.observeOnLifecycleStop(viewLifecycleOwner) {
            setShimmer(false)

            binding.isResult = when {
                it.refresh is LoadState.Loading -> {
                    setShimmer(true)
                    true
                }

                it.refresh is LoadState.Error -> {
                    binding.noResult.errorString = getString(R.string.str_load_state_error)
                    false
                }

                it.refresh is LoadState.NotLoading && adapter.itemCount == 0 -> {
                    binding.noResult.errorString = getString(R.string.str_no_result)
                    false
                }

                else -> {
                    true
                }
            }
        }
    }

    override fun initViewModelCallback(): Unit = viewModel.run {
        pointList.observeOnLifecycleStop(viewLifecycleOwner) {
            adapter.submitData(it)
            viewModel.hideLoadingDialog()
        }

        onUsePointClickChannel.onEach {
            findNavController().safeNavigate(
                PointFragmentDirections.actionGlobalBasicDialog(
                    BasicDialogModel(titleText = resources.getString(R.string.str_use_point_title),
                        text = resources.getString(R.string.str_use_point_not_yet),
                        setPositiveButton = true to resources.getString(R.string.confirm),
                        buttonClickListener = object : BasicDialog.ButtonClickListener {
                            override fun onPositiveButtonClick(
                                view: View, dialog: BasicDialog
                            ) {
                                super.onPositiveButtonClick(view, dialog)
                                dialog.dismiss()
                            }
                        }),
                )
            )
        }.observeInLifecycleStop(viewLifecycleOwner)
    }

    override fun initMainViewModelCallback(): Unit = with(mainViewModel) {
        scrollTop.onEach {
            binding.rv.stopScroll()
            binding.rv.smoothScrollToPosition(0)
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
        adapter.refresh()
    }
}