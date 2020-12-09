package com.louis.gourmandism.event.item

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        binding.lifecycleOwner = this

        val adapter = EventItemAdapter(viewModel)
        binding.recyclerViewEvent.adapter = adapter

        viewModel.liveEventList.observe(viewLifecycleOwner, Observer {

            adapter.submitList(it)
        })

//        viewModel.eventList.observe(viewLifecycleOwner, Observer {
//            it?.let{
//                adapter.submitList(it)
//            }
//        })


        viewModel.shopInfo.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(EventFragmentDirections.actionGlobalDetailFragment(it.id))
        })

        viewModel.toastStatus.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(),"人數已滿",Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

}