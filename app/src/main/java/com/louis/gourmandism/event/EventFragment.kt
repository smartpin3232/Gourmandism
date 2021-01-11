package com.louis.gourmandism.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.FragmentEventBinding

class EventFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEventBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        viewPager.adapter = PagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.openList)
                1 -> tab.text = getString(R.string.myEvent)
            }
            viewPager.setCurrentItem(tab.position, true)
        }.attach()

        return binding.root
    }

}