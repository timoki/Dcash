package com.dmonster.dcash.view.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmonster.dcash.databinding.ItemNewsListBinding
import com.dmonster.domain.model.paging.news.NewsListModel

class NewsAdapter(
    private val onItemClick: (item: NewsListModel) -> Unit
) : PagingDataAdapter<NewsListModel, NewsAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemNewsListBinding =
            ItemNewsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return, onItemClick)
    }

    inner class ViewHolder(
        private val binding: ItemNewsListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            model: NewsListModel, onItemClick: (item: NewsListModel) -> Unit
        ) {
            binding.data = model
            binding.root.setOnClickListener {
                onItemClick.invoke(
                    model
                )
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NewsListModel>() {
            override fun areItemsTheSame(oldItem: NewsListModel, newItem: NewsListModel): Boolean {
                return oldItem.guid == newItem.guid
            }

            override fun areContentsTheSame(
                oldItem: NewsListModel,
                newItem: NewsListModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}