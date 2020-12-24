package com.louis.gourmandism.search

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.ItemTagBinding


class SearchAdapter(private val viewModel: SearchViewModel) :
    ListAdapter<String, SearchAdapter.ViewHolder>(DiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: String,
            viewModel: SearchViewModel,
            position: Int
        ) {
            binding.tag = item

            if(position == viewModel.tagPosition.value){
                binding.textType.setBackgroundColor(Color.parseColor("#cbae82"))
            }else{
                binding.textType.setBackgroundColor(Color.parseColor("#ffe0b2"))
            }

            binding.textType.setOnClickListener {
                if(item == "新增"){
                    viewModel.addSelectTag()
                    viewModel.tagPosition.value = null
                }else{
                    viewModel.markerSet(item, position)
                }
            }

            binding.textType.setOnLongClickListener {
                if(item != "新增"){
                    viewModel.setUserTag(item)
                }
                true
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTagBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}