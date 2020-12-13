package com.louis.gourmandism.comment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        binding.recyclerViewComment.adapter = adapter

        viewModel.commentInfo.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.images)
            viewModel.getProfile(it.host!!.id)
            viewModel.setStar(images,it.star.toInt())
        })


        return binding.root
    }

}