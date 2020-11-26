package com.louis.gourmandism.event

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.FragmentEventBinding

class EventFragment : Fragment() {

    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventBinding.inflate(inflater,container,false)
        return binding.root
    }

}