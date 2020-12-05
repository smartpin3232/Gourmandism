package com.louis.gourmandism.wish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.louis.gourmandism.databinding.FragmentWishBinding
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.home.HomeViewModel

class WishFragment : Fragment() {

    private val viewModel by viewModels<WishViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWishBinding.inflate(inflater,container,false)


        return binding.root
    }

}