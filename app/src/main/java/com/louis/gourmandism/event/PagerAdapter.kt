package com.louis.gourmandism.event

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.louis.gourmandism.event.item.EventItemFragment


class PagerAdapter(fragment:Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
       return EventItemFragment(position)
    }

}