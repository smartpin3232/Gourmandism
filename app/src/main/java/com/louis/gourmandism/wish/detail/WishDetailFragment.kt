package com.louis.gourmandism.wish.detail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.databinding.FragmentWishDetailBinding
import com.louis.gourmandism.extension.getVmFactory


class WishDetailFragment : Fragment() {

    private val viewModel by viewModels<WishDetailViewModel> {
        getVmFactory(
            WishDetailFragmentArgs.fromBundle(
                requireArguments()
            ).favorite
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWishDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = WishDetailAdapter(viewModel)
        binding.recyclerViewFavorite.adapter = adapter

        viewModel.favoriteInfo.observe(viewLifecycleOwner, Observer {

            viewModel.getUser(it.userId)
        })

        viewModel.shop.observe(viewLifecycleOwner, Observer {

            viewModel.favoriteInfo.value?.shops?.let {
                adapter.submitList(viewModel.getNewShop(it))
            }
        })

        viewModel.navigateInfo.observe(viewLifecycleOwner, Observer {

            findNavController().navigate(NavigationDirections.actionGlobalDetailFragment(it.id))
        })

        return binding.root
    }

}