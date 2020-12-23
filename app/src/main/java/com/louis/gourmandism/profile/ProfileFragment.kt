package com.louis.gourmandism.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.FragmentProfileBinding
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.login.UserManager
import kotlinx.android.synthetic.main.fragment_profile.view.*

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

        binding.buttonAddFriend.setOnClickListener {

            if(binding.buttonAddFriend.tag == true){
                viewModel.addFriend(false)
                binding.buttonAddFriend.text = getString(R.string.add_friend)
                binding.buttonAddFriend.tag = false
            } else{
                viewModel.addFriend(true)
                binding.buttonAddFriend.text = getString(R.string.delete_friend)
                binding.buttonAddFriend.tag = true
            }

        }

        binding.buttonEdit.setOnClickListener {
            viewModel.profile.value?.let {
                findNavController().navigate(NavigationDirections.actionGlobalProfileEditDialog(it))
            }
        }

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
                viewModel.onNavigationDone()
            }
        })

        viewModel.shopInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(ProfileFragmentDirections.actionGlobalDetailFragment(it.id))
            }
        })

        viewModel.masterInfo.observe(viewLifecycleOwner, Observer {
            it.friendList?.let {friendId->
                if(friendId.any {data-> data == userId }){
                    binding.buttonAddFriend.text = getString(R.string.delete_friend)
                    binding.buttonAddFriend.tag = true
                }else{
                    binding.buttonAddFriend.text = getString(R.string.add_friend)
                    binding.buttonAddFriend.tag = false
                }
            }
        })

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
}