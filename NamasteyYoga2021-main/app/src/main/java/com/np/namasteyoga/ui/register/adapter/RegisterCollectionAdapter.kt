package com.np.namasteyoga.ui.register.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.np.namasteyoga.ui.register.fragments.CenterFragment
import com.np.namasteyoga.ui.register.fragments.GuestUserFragment
import com.np.namasteyoga.ui.register.fragments.TrainerFragment

class RegisterCollectionAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3
    private val fragmentList = listOf(TrainerFragment(),CenterFragment(),GuestUserFragment())

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
    companion object{
        private const val ARG_OBJECT = "object"
    }
}