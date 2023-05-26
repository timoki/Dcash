package com.dmonster.dcash.view.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmonster.dcash.databinding.ItemNewsListBinding
import com.dmonster.domain.model.paging.news.NewsListModel

internal class NewsAdapter : PagingDataAdapter<NewsListModel, NewsAdapter.ViewHolder>(diffUtil) {

    private var listener: ItemListener? = null

    fun setOnItemClickListener(itemClickListener: ItemListener) {
        this.listener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemNewsListBinding =
            ItemNewsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listener?.let {
            holder.bind(getItem(position) ?: return, it)
        }
    }

    inner class ViewHolder(
        private val binding: ItemNewsListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            model: NewsListModel, listener: ItemListener
        ) {
            binding.data = model
            binding.listener = listener
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

    interface ItemListener {
        fun onRootClick(item: NewsListModel)
    }
}