package com.np.namasteyoga.ui.quiz.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.np.namasteyoga.R
import kotlinx.android.synthetic.main.event_details_layout.*
import kotlinx.android.synthetic.main.poll_bottomsheet_layout.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.ParseException
import java.text.SimpleDateFormat


//import kotlinx.android.synthetic.main.fragment_events.*

class QuizBottomsheet : BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.quiz_bottomsheet_layout, container, false)
    }

    fun setData(eventType: String) {
//        tvAlreadydone.text = getString(R.string.already_submitted)
        tvAlreadydone.text = eventType
        tvOK.onClick {
            //TODO close bottomsheet
            mBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                mBehavior = (this@QuizBottomsheet.dialog as BottomSheetDialog).behavior
                mBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
    }

    private var mBehavior: BottomSheetBehavior<FrameLayout>? = null

    override fun onResume() {
        super.onResume()
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }


    private fun convertStringToTimestamp(str_date: String?): Long {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = formatter.parse(str_date)
            date.time
        } catch (e: ParseException) {
            println("Exception :$e")
            0
        }
    }

}