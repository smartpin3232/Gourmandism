package com.louis.gourmandism.wish.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.databinding.ItemFavoriteBinding

class WishDetailAdapter(private val viewModel: WishDetailViewModel) :
    ListAdapter<Shop, WishDetailAdapter.ViewHolder>(DiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel,holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(shop: Shop, viewModel: WishDetailViewModel,holder: ViewHolder) {
            binding.shop = shop
            holder.itemView.setOnClickListener {
                viewModel.navigateToDetail(shop)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemFavoriteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Shop>() {
    override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean {
        return oldItem == newItem
    }
}