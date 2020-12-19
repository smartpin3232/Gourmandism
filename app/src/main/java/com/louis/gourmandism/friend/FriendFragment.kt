package com.louis.gourmandism.friend

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.FragmentFriendBinding
import kotlinx.android.synthetic.main.fragment_search.*

class FriendFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFriendBinding.inflate(inflater, container, false)

        return binding.root
    }

}