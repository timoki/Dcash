package com.dmonster.dcash.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmonster.dcash.R
import com.dmonster.dcash.databinding.ItemLoadStateFooterViewBinding

class MyLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<MyLoadStateAdapter.LoadStateViewHolder>() {

    private lateinit var binding: ItemLoadStateFooterViewBinding

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_load_state_footer_view,
            parent,
            false
        )
        val errorString = parent.context.resources.getString(R.string.str_load_state_error)
        return LoadStateViewHolder(binding, errorString, retry)
    }

    inner class LoadStateViewHolder(
        private val binding: ItemLoadStateFooterViewBinding,
        private val errorString: String,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.NotLoading && loadState.endOfPaginationReached) {
                binding.isVisible = false
                return
            }

            if (loadState is LoadState.Error) {
                binding.errorMsg.text = errorString
            }

            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }
}