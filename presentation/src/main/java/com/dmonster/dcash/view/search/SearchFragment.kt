package com.dmonster.dcash.view.search

import android.os.Build
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.dmonster.dcash.base.BaseFragment
import com.dmonster.dcash.databinding.FragmentSearchBinding
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.view.dialog.select.adapter.SelectDialogData
import com.dmonster.dcash.view.news.NewsFragment
import com.dmonster.dcash.view.news.NewsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {
    override fun init() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.textInputEt.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                setFragmentResult(
                    NewsFragment.TAG,
                    bundleOf(
                        "search_filter" to viewModel.selectType.value.code,
                        "search_value" to viewModel.searchText.value
                    )
                )
                findNavController().popBackStack()
                mainViewModel.onBottomMenuClick(1)
                true
            }

            false
        }

        setFragmentResultListener(TAG) { _, result ->
            val selectItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.getParcelable("selectItem", SelectDialogData::class.java)
            } else {
                result.getParcelable("selectItem") as SelectDialogData?
            }

            viewModel.selectType.value = selectItem ?: return@setFragmentResultListener
            viewModel.selectTypeText.value = selectItem.item
        }
    }

    override fun initViewModelCallback(): Unit = viewModel.run {
        searchTypeClickChannel.onEach {
            findNavController().safeNavigate(
                NewsFragmentDirections.actionGlobalSelectDialog(
                    title = "정렬",
                    list = arrayOf(
                        SelectDialogData("all", "통합검색", selectType.value.code == "all"),
                        SelectDialogData("title", "제목", selectType.value.code == "title"),
                        SelectDialogData("author", "기자", selectType.value.code == "author"),
                    ),
                    requestTag = TAG
                )
            )
        }.observeInLifecycleStop(viewLifecycleOwner)
    }

    companion object {
        const val TAG = "SearchFragment"
    }
}