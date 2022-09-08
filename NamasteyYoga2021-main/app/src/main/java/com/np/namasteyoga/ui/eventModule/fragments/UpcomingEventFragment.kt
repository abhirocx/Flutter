package com.np.namasteyoga.ui.eventModule.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.np.namasteyoga.R
import com.np.namasteyoga.utils.hide
import com.np.namasteyoga.utils.show
import kotlinx.android.synthetic.main.fragment_past_event.*


class UpcomingEventFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_past_event, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        llNoEvents.show()
        isShowDialog()
    }
    private fun isShowDialog(b: Boolean=true) {
        if (b) {
            progress_circular.hide()
        } else {
            progress_circular.show()
        }
    }
    companion object {
        const val TAG = "UpcomingEventFragment"
    }
}