package com.louis.gourmandism.add2wish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.louis.gourmandism.data.Favorite
import com.louis.gourmandism.databinding.ItemAdd2wishBinding

class Add2wishAdapter(private val viewModel: Add2wishViewModel) :
    ListAdapter<Favorite, Add2wishAdapter.ViewHolder>(DiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel,holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemAdd2wishBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Favorite,
            viewModel: Add2wishViewModel,
            holder: ViewHolder
        ) {
            binding.item = item
            holder.itemView.setOnClickListener {
                viewModel.setWish(item.id,1)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAdd2wishBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Favorite>() {
    override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem == newItem
    }
}