package com.louis.gourmandism.event.item

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.louis.gourmandism.data.Event
import com.louis.gourmandism.databinding.ItemEventBinding
import com.louis.gourmandism.login.UserManager
import java.text.SimpleDateFormat
import java.util.*

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

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(event: Event, viewModel: EventItemViewModel) {
            binding.event = event

            eventStatusInit(event)

            val dateString = SimpleDateFormat("MM/dd/yyyy HH:mm").format(Date(event.time))
            binding.textTime.text = "時間: $dateString"

            binding.textShopInfo.setOnClickListener {
                viewModel.toShop(event)
            }

            binding.textAdd.setOnClickListener {
                changeEventStatus(event, viewModel)
            }

            binding.executePendingBindings()
        }

        private fun changeEventStatus(
            item: Event,
            viewModel: EventItemViewModel
        ) {
            if (binding.textAdd.text == "加入") {
                if (item.member!!.size >= item.memberLimit) {
                    viewModel.toast()
                } else {
                    binding.textAdd.text = "退出"
                    viewModel.join.value = true
                    viewModel.joinGame(item.id, 0)
                    viewModel.setEventNotification(item)
                }
            } else if (binding.textAdd.text == "退出") {
                binding.textAdd.text = "加入"
                viewModel.leave.value = true
                viewModel.joinGame(item.id, 1)
            } else {
                item.shop?.let {
                    viewModel.navigateToNewComment(it)
                }
            }
        }

        private fun eventStatusInit(item: Event) {
            if (item.time <= System.currentTimeMillis()) {
                binding.textAdd.text = "給予評論"
            } else {
                if (item.member?.any { it == UserManager.userToken }!!) {
                    binding.textAdd.text = "退出"
                } else {
                    binding.textAdd.text = "加入"
                }
            }
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