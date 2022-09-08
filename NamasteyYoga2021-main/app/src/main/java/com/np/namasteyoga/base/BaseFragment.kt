package com.np.namasteyoga.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.np.namasteyoga.interfaces.FragmentImplement

abstract class BaseFragment :Fragment() , FragmentImplement {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout(), container, false)

    }
}