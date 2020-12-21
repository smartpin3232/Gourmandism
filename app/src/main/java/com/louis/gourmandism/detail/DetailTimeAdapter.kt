package com.louis.gourmandism.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.louis.gourmandism.data.OpenTime
import com.louis.gourmandism.databinding.ItemTimeBinding

class DetailTimeAdapter(private val viewModel: DetailViewModel) :
    ListAdapter<OpenTime, DetailTimeAdapter.ViewHolder>(TimeDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel,holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: OpenTime, viewModel: DetailViewModel, holder: ViewHolder
        ) {
            binding.time = item
            holder.itemView.setOnClickListener {

            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTimeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TimeDiffCallback : DiffUtil.ItemCallback<OpenTime>() {
    override fun areItemsTheSame(oldItem: OpenTime, newItem: OpenTime): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: OpenTime, newItem: OpenTime): Boolean {
        return oldItem == newItem
    }
}