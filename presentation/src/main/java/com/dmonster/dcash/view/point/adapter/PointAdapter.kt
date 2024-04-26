package com.dmonster.dcash.view.point.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmonster.dcash.databinding.ItemPointHistoryBinding
import com.dmonster.domain.model.paging.point.PointHistoryModel

class PointAdapter : PagingDataAdapter<PointHistoryModel, PointAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPointHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    inner class ViewHolder(
        private val binding: ItemPointHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(
            item: PointHistoryModel,
        ) {
            binding.model = item
        }
    }
    
    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PointHistoryModel>() {
            override fun areItemsTheSame(
                oldItem: PointHistoryModel,
                newItem: PointHistoryModel
            ): Boolean {
                return oldItem.ptWdate == newItem.ptWdate
            }

            override fun areContentsTheSame(
                oldItem: PointHistoryModel,
                newItem: PointHistoryModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}