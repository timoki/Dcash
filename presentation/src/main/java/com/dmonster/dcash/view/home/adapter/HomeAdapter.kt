package com.dmonster.dcash.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dmonster.dcash.databinding.ItemHomeBannerBinding
import com.dmonster.dcash.databinding.ItemNewsListBigBinding
import com.dmonster.dcash.databinding.ItemNewsListBinding
import com.dmonster.dcash.databinding.ItemNewsListCustomBinding
import com.dmonster.domain.model.paging.news.NewsListModel

class HomeAdapter(
    private val itemViewType: Int, private val onItemClick: (item: NewsListModel) -> Unit
) : ListAdapter<NewsListModel, HomeAdapter.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return itemViewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewBinding = when (viewType) {
            TYPE_BANNER -> {
                ItemHomeBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }

            TYPE_RECOMMEND_BIG -> {
                ItemNewsListBigBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }

            TYPE_CUSTOM -> {
                ItemNewsListCustomBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            }

            else -> {
                ItemNewsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }
        }
        return ViewHolder(binding, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            currentList[position], onItemClick
        )
    }

    inner class ViewHolder(
        private val binding: ViewBinding,
        private val itemViewType: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: NewsListModel, onItemClick: (item: NewsListModel) -> Unit
        ) {
            when (itemViewType) {
                TYPE_BANNER -> {
                    (binding as ItemHomeBannerBinding).run {
                        image = item.enclosure
                        title = item.title
                        creator = item.creator
                        author = item.author
                        date = item.pubDate
                        point = item.point
                    }
                }

                TYPE_RECOMMEND_BIG -> {
                    (binding as ItemNewsListBigBinding).run {
                        data = item
                    }
                }

                TYPE_CUSTOM -> {
                    (binding as ItemNewsListCustomBinding).run {
                        data = item
                    }
                }

                else -> {
                    (binding as ItemNewsListBinding).run {
                        data = item
                    }
                }
            }

            binding.root.setOnClickListener {
                onItemClick.invoke(
                    item
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
                oldItem: NewsListModel, newItem: NewsListModel
            ): Boolean {
                return oldItem == newItem
            }
        }

        const val TYPE_BANNER = 0
        const val TYPE_RECOMMEND_BIG = 1
        const val TYPE_RECOMMEND = 2
        const val TYPE_CUSTOM = 3
        const val TYPE_CATEGORY = 4
    }
}