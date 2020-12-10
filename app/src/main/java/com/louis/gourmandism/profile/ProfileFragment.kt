package com.louis.gourmandism.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.louis.gourmandism.databinding.FragmentProfileBinding
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.home.HomeViewModel

class ProfileFragment : Fragment(){

    private val viewModel by viewModels<ProfileViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = ProfileCommentAdapter(viewModel)
        binding.recyclerViewProfileComment.adapter = adapter

        val previewAdapter = ProfilePreviewAdapter(viewModel)
        binding.recyclerViewPreview.adapter = previewAdapter

        viewModel.comment.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.profile.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.browseRecently?.let { data -> viewModel.getShop(data) }
            }
        })

        viewModel.shop.observe(viewLifecycleOwner, Observer {
            it?.let{
                previewAdapter.submitList(it)
            }
        })

        return binding.root
    }
}