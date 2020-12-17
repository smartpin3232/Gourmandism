package com.louis.gourmandism.event.item

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.louis.gourmandism.EventReceiver

import com.louis.gourmandism.databinding.FragmentEventItemBinding
import com.louis.gourmandism.event.EventFragmentDirections
import com.louis.gourmandism.extension.getVmFactory
import java.util.*

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

        viewModel.notificationInfo.observe(viewLifecycleOwner, Observer {

            val intent = Intent(requireContext(), EventReceiver::class.java)
            intent.action = "Event"
//            val bundle = Bundle()
//            bundle.putString("EventId",it.id)
            intent.putExtra("EventId",it.id)
            val sender = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
            val am = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

            am.set(AlarmManager.RTC_WAKEUP,it.time,sender)

        })

        return binding.root
    }

}