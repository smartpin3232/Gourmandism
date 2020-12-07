package com.louis.gourmandism.event.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.FragmentEventItemBinding
import com.louis.gourmandism.event.EventFragmentDirections
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.home.HomeViewModel

class EventItemFragment(val status: Int) : Fragment() {

    private val viewModel by viewModels<EventItemViewModel> { getVmFactory(status) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventItemBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = EventItemAdapter(viewModel)
        binding.recyclerViewEvent.adapter = adapter

        viewModel.eventList.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        viewModel.shopInfo.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(EventFragmentDirections.actionGlobalDetailFragment(it.id))
        })

        return binding.root
    }

}