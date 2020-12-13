package com.louis.gourmandism.wish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.databinding.FragmentWishBinding
import com.louis.gourmandism.databinding.ItemWishBinding
import com.louis.gourmandism.extension.getVmFactory

class WishFragment : Fragment() {

    private val viewModel by viewModels<WishViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWishBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this


        val adapter = WishAdapter(viewModel)
        binding.recyclerViewMyWishList.adapter = adapter

        viewModel.myFavorite.observe(viewLifecycleOwner, Observer {
            viewModel.getShop()

        })

        viewModel.shop.observe(viewLifecycleOwner, Observer {
            it?.let{

                adapter.submitList(viewModel.getNewFavorite())
            }
        })

        binding.textNew.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalNewWishListDialog())
        }



        viewModel.navigationData.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(NavigationDirections.actionGlobalWishDetailFragment(it))
        })


        return binding.root
    }

}