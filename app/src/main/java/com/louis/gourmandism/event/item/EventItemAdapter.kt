package com.louis.gourmandism.event.item

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.louis.gourmandism.data.Event
import com.louis.gourmandism.databinding.ItemEventBinding
import com.louis.gourmandism.login.UserManager
import kotlinx.android.synthetic.main.item_event.view.*

class EventItemAdapter(private val viewModel: EventItemViewModel) :
    ListAdapter<Event, EventItemAdapter.ViewHolder>(DiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: Event, viewModel: EventItemViewModel) {
            binding.event = item

            binding.textShopInfo.setOnClickListener {
                viewModel.toShop(item)
            }

            if(item.member?.any { it == UserManager.userToken }!!){
                binding.textAdd.text = "退出"
            }else{
                binding.textAdd.text = "加入"
            }


            binding.textAdd.setOnClickListener {
                if (binding.textAdd.text == "加入"){
                    if(item.member!!.size >= item.memberLimit){
                        viewModel.toast()
                    }else{
                        binding.textAdd.text = "退出"
                        viewModel.joinGame(item.id,0)
                    }
                }else{
                    binding.textAdd.text = "加入"
                    viewModel.joinGame(item.id,1)
                }
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemEventBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }
}

class DiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}