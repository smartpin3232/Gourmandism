package com.louis.gourmandism.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.louis.gourmandism.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }
}