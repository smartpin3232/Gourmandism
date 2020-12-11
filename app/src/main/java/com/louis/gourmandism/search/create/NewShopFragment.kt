package com.louis.gourmandism.search.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.databinding.FragmentNewShopBinding
import com.louis.gourmandism.detail.DetailFragmentArgs
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.extension.newShopVmFactory
import com.louis.gourmandism.wish.WishViewModel

class NewShopFragment : Fragment() {

    private val viewModel by viewModels<NewShopViewModel> {
        newShopVmFactory(
            NewShopFragmentArgs.fromBundle(requireArguments()).location
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewShopBinding.inflate(inflater, container, false)
        binding.buttonCreate.setOnClickListener {
            viewModel.newShop(
                binding.editName.text.toString(),
                binding.editAddress.text.toString(),
                binding.editPhone.text.toString()
            )
        }
        viewModel.newStatus.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(NavigationDirections.actionGlobalSearchFragment())
        })
        return binding.root
    }


}