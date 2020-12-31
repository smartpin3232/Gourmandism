package com.louis.gourmandism.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.louis.gourmandism.R
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.databinding.ItemSearchListBinding

class SearchListAdapter(private val viewModel: SearchViewModel) :
    ListAdapter<Shop, SearchListAdapter.ViewHolder>(SearchListDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel, holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemSearchListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            shop: Shop,
            viewModel: SearchViewModel,
            holder: ViewHolder
        ) {
            binding.shop = shop

            viewModel.myFavoriteShop.value?.let {favoriteList->
                initWishStatus(favoriteList, shop)
            }

            binding.constraintShopInfo.setOnClickListener {
                viewModel.navigateToDetail(shop)
            }

            binding.imageWish.setOnClickListener {
                viewModel.navigateToAddWish(shop)
            }

            binding.executePendingBindings()
        }

        private fun initWishStatus(
            it: MutableList<String>,
            shop: Shop
        ) {
            if (it.any { shopId -> shopId == shop.id }) {
                binding.imageWish.setBackgroundResource(R.drawable.wish_selected)
            } else {
                binding.imageWish.setBackgroundResource(R.drawable.wish)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SearchListDiffCallback : DiffUtil.ItemCallback<Shop>() {
    override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean {
        return oldItem == newItem
    }
}