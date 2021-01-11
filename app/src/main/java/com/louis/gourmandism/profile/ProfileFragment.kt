package com.louis.gourmandism.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.FragmentProfileBinding
import com.louis.gourmandism.event.EventFragmentDirections
import com.louis.gourmandism.event.EventJoinDialog
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.login.UserManager

class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel> {
        getVmFactory(ProfileFragmentArgs.fromBundle( requireArguments()).userId )
    }
    lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val userId = ProfileFragmentArgs.fromBundle( requireArguments()).userId

        val adapter = ProfileCommentAdapter(viewModel)
        binding.recyclerViewProfileComment.adapter = adapter

        val previewAdapter = ProfilePreviewAdapter(viewModel)
        binding.recyclerViewPreview.adapter = previewAdapter

        viewModel.comment.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                binding.textLikeAmount.text = viewModel.getLikeAmount(it).toString()
            }
        })

        viewModel.myFavorite.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.textShareAmount.text = viewModel.getForkAmount(it).toString()
            }
        })

        viewModel.profile.observe(viewLifecycleOwner, Observer {
            it?.let {
                checkUserPermission(it.id)
                it.browseRecently?.let { data -> viewModel.getShop(data) }
            }
        })

        viewModel.shop.observe(viewLifecycleOwner, Observer {
            it?.let {
                previewAdapter.submitList(it)
            }
        })

        viewModel.commentInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(ProfileFragmentDirections.actionGlobalCommentFragment(it))
                viewModel.onNavigated()
            }
        })

        viewModel.shopInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(ProfileFragmentDirections.actionGlobalDetailFragment(it.id))
                viewModel.onNavigated()
            }
        })

        viewModel.masterInfo.observe(viewLifecycleOwner, Observer {
            it.friendList?.let {friendId->
                if(friendId.any {data-> data == userId }){
                    changeFriendStatus(true)
                }else{
                    changeFriendStatus(false)
                }
            }
        })

        binding.buttonAddFriend.setOnClickListener {
            if(binding.buttonAddFriend.tag == true){
                changeFriendStatus(true)
                viewModel.setFriend(true)
                findNavController().navigate(
                    EventFragmentDirections.actionGlobalEventJoinDialog(EventJoinDialog.MessageType.JOIN)
                )
            } else{
                changeFriendStatus(false)
                viewModel.setFriend(false)
                findNavController().navigate(
                    EventFragmentDirections.actionGlobalEventJoinDialog(EventJoinDialog.MessageType.CANCEL)
                )
            }
        }

        binding.buttonEdit.setOnClickListener {
            viewModel.profile.value?.let {
                findNavController().navigate(NavigationDirections.actionGlobalProfileEditDialog(it))
            }
        }

        return binding.root
    }

    private fun checkUserPermission(userId: String){
        if(userId == UserManager.userToken){
            binding.buttonAddFriend.visibility = View.GONE
            binding.buttonSendMessage.visibility = View.GONE
            binding.buttonEdit.visibility = View.VISIBLE
        }else{
            binding.buttonAddFriend.visibility = View.VISIBLE
            binding.buttonSendMessage.visibility = View.VISIBLE
            binding.buttonEdit.visibility = View.GONE
        }
    }

    private fun changeFriendStatus(status: Boolean) {
        binding.buttonAddFriend.apply {
            text = if(status){
                getString(R.string.delete_friend)
            } else {
                getString(R.string.add_friend)
            }
            tag = !status
        }
    }

}