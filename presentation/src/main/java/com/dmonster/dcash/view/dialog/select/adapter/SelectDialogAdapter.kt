package com.dmonster.dcash.view.dialog.select.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmonster.dcash.databinding.ItemSelectListBinding

class SelectDialogAdapter(
    private val itemClick: (SelectDialogData) -> Unit
) : ListAdapter<SelectDialogData, SelectDialogAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            currentList[position],
            itemClick
        )
    }

    inner class ViewHolder(
        private val binding: ItemSelectListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: SelectDialogData,
            itemClick: (SelectDialogData) -> Unit
        ) {
            binding.item = item.item
            binding.isSelected = item.isSelected

            binding.root.setOnClickListener {
                itemClick.invoke(item)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<SelectDialogData>() {
            override fun areItemsTheSame(oldItem: SelectDialogData, newItem: SelectDialogData): Boolean {
                return oldItem.item == newItem.item
            }

            override fun areContentsTheSame(oldItem: SelectDialogData, newItem: SelectDialogData): Boolean {
                return oldItem == newItem
            }
        }
    }
}