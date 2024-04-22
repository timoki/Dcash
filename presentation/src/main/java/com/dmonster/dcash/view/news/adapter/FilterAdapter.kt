package com.dmonster.dcash.view.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmonster.dcash.databinding.ItemFilterCheckboxBinding
import com.dmonster.domain.model.home.FilterModel

class FilterAdapter(
    private val onItemClick: (item: FilterModel) -> Unit
) : ListAdapter<FilterModel, FilterAdapter.FilterAdapterViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapterViewHolder {
        val binding = ItemFilterCheckboxBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return FilterAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterAdapterViewHolder, position: Int) {
        holder.bind(
            currentList[position],
            onItemClick
        )
    }

    inner class FilterAdapterViewHolder(
        private val binding: ItemFilterCheckboxBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: FilterModel,
            onItemClick: (item: FilterModel) -> Unit
        ) {
            binding.name = item.name
            binding.isChecked = item.isChecked

            binding.root.setOnClickListener {
                item.isChecked = !item.isChecked
                onItemClick.invoke(item)
                notifyItemChanged(absoluteAdapterPosition)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<FilterModel>() {
            override fun areItemsTheSame(oldItem: FilterModel, newItem: FilterModel): Boolean {
                return oldItem.code == newItem.code
            }

            override fun areContentsTheSame(oldItem: FilterModel, newItem: FilterModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}