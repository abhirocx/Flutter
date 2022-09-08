package com.np.namasteyoga.ui.onPaperBoard.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ScreenSlidePagerAdapter(fragment: FragmentActivity,private val list: ArrayList<Fragment>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
//        return ViewPagerContentFragment.newInstance(list[position])
        return list[position]
    }

}