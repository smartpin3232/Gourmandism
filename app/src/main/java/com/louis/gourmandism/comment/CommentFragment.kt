package com.louis.gourmandism.comment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.louis.gourmandism.databinding.FragmentCommentBinding
import com.louis.gourmandism.extension.getCommentVmFactory

class CommentFragment : Fragment() {

    private val viewModel by viewModels<CommentViewModel> { getCommentVmFactory(CommentFragmentArgs.fromBundle(requireArguments()).comment) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCommentBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val images = mutableListOf(
            binding.imageLevelOne,
            binding.imageLevelTwo,
            binding.imageLevelThree,
            binding.imageLevelFour,
            binding.imageLevelFive
        )

        val adapter = CommentAdapter(viewModel)
        binding.recyclerViewCommentImage.adapter = adapter

        viewModel.commentInfo.observe(viewLifecycleOwner, Observer {

            it.host?.let {user->
                viewModel.getProfile(user.id)
            }
            adapter.submitList(it.images)
            viewModel.setStar(images, it.star)
        })


        return binding.root
    }

}