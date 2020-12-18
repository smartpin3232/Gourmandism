package com.louis.gourmandism.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.louis.gourmandism.R
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.databinding.ItemHomeBinding
import com.louis.gourmandism.login.UserManager

class HomeAdapter(private val viewModel: HomeViewModel) :
    ListAdapter<Comment, HomeAdapter.ViewHolder>(DiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Comment, viewModel: HomeViewModel) {
            binding.data = item

            item.like?.let {
                if(it.any {likeList-> likeList == UserManager.userToken }){
                    binding.imageFavorite.tag = true
                    binding.imageFavorite.setBackgroundResource(R.drawable.good_select)
                }else{
                    binding.imageFavorite.tag = false
                    binding.imageFavorite.setBackgroundResource(R.drawable.good)
                }
            }


            binding.imageFavorite.setOnClickListener {
                if(it.tag == true){
                    it.tag = false
                    viewModel.setLike(item.commentId,0)
                    it.setBackgroundResource(R.drawable.good)
                }else{
                    it.tag = true
                    viewModel.setLike(item.commentId,1)
                    it.setBackgroundResource(R.drawable.good_select)
                }
            }
            binding.textComment.setOnClickListener {
                viewModel.navigateToComment(item)
            }

            binding.textTitle.setOnClickListener {
                viewModel.navigationToDetail(item.shopId)
            }

            binding.userImage.setOnClickListener {
                viewModel.navigateToProfile(item.host!!.id)
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHomeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}