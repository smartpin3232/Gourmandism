package com.louis.gourmandism.wish

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.FragmentWishBinding

class WishFragment : Fragment() {

    private lateinit var viewModel: WishViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWishBinding.inflate(inflater,container,false)
        return binding.root
    }

}