package com.louis.gourmandism.friend.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.databinding.FragmentFriendItemBinding
import com.louis.gourmandism.event.item.EventItemViewModel
import com.louis.gourmandism.extension.getVmFactory

class FriendItemFragment(val type: Int) : Fragment() {

    private val viewModel by viewModels<FriendItemViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding= FragmentFriendItemBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val adapter = FriendAdapter(viewModel)
        binding.recyclerViewFriend.adapter = adapter

        viewModel.profile.observe(viewLifecycleOwner, Observer {
            it.friendList?.let { friendList ->
                if(!friendList.isNullOrEmpty()){
                    viewModel.getAllFriend(friendList)
                }
            }
        })

        viewModel.friendInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(type == 0){
                    adapter.submitList(it)
                }
            }
        })

        viewModel.navigateProfile.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalProfileFragment(it.id))
                viewModel.onNavigateDone()
            }
        })

        return binding.root
    }

}