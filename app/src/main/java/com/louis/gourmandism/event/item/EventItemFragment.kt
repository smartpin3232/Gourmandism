package com.louis.gourmandism.event.item

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.louis.gourmandism.EventReceiver
import com.louis.gourmandism.data.Event
import com.louis.gourmandism.databinding.FragmentEventItemBinding
import com.louis.gourmandism.event.EventFragmentDirections
import com.louis.gourmandism.event.EventJoinDialog
import com.louis.gourmandism.extension.getVmFactory

class EventItemFragment(val status: Int) : Fragment() {

    private val viewModel by viewModels<EventItemViewModel> { getVmFactory(status) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventItemBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val adapter = EventItemAdapter(viewModel)
        binding.recyclerViewEvent.adapter = adapter

        viewModel.liveEventList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(viewModel.filterList(it))
            }
        })

        viewModel.shopInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(EventFragmentDirections.actionGlobalDetailFragment(it.id))
                viewModel.onNavigated()
            }
        })

        viewModel.navigateToNewCommentInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    EventFragmentDirections.actionGlobalAdd2commentDialog(
                        it
                    )
                )
                viewModel.onNavigated()
            }
        })

        viewModel.notificationInfo.observe(viewLifecycleOwner, Observer {
            setNotification(it)
        })

        viewModel.join.observe(viewLifecycleOwner, Observer {
            it.let {
                findNavController().navigate(
                    EventFragmentDirections.actionGlobalEventJoinDialog(EventJoinDialog.MessageType.JOIN)
                )
            }
        })

        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it.let {
                findNavController().navigate(
                    EventFragmentDirections.actionGlobalEventJoinDialog(EventJoinDialog.MessageType.LEAVE)
                )
            }
        })

        viewModel.toastStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    EventFragmentDirections.actionGlobalEventJoinDialog(EventJoinDialog.MessageType.FULL)
                )
            }
        })

        return binding.root
    }

    private fun setNotification(event: Event) {

        val intent = Intent(requireContext(), EventReceiver::class.java)
        intent.action = "Event"
        intent.putExtra("EventId", event.id)
        intent.putExtra("shopName", event.shop?.name)

        val sender = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        val am = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.set(AlarmManager.RTC_WAKEUP, event.time, sender)
    }

}
