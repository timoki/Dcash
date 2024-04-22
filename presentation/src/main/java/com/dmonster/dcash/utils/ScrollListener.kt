package com.dmonster.dcash.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmonster.dcash.view.main.MainViewModel
import javax.inject.Inject

class ScrollListener @Inject constructor(
    private val layoutManager: LinearLayoutManager,
    private val mainViewModel: MainViewModel
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        layoutManager.apply {
            val firstItemPosition = findFirstVisibleItemPosition()
            mainViewModel.topButtonVisible.value = firstItemPosition >= 2
        }
    }
}