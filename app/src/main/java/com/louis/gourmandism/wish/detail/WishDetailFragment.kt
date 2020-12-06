package com.louis.gourmandism.wish.detail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.louis.gourmandism.databinding.FragmentWishDetailBinding
import com.louis.gourmandism.extension.getVmFactory


class WishDetailFragment : Fragment() {

    private val viewModel by viewModels<WishDetailViewModel> { getVmFactory(WishDetailFragmentArgs.fromBundle(requireArguments()).favorite) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWishDetailBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = WishDetailAdapter(viewModel)
        binding.recyclerViewFavorite.adapter = adapter

        viewModel.favoriteInfo.observe(viewLifecycleOwner, Observer {
            viewModel.getUser(it.userId)
            adapter.submitList(it.shops)
        })

        return binding.root
    }

}