package com.np.namasteyoga.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialogFragment
import com.np.namasteyoga.R
import com.np.namasteyoga.interfaces.ConfirmationListener
import kotlinx.android.synthetic.main.button_colored_layout.*

class GetStartBottomSheet(private var confirmationListener: ConfirmationListener): RoundedBottomSheetDialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.get_start_splash_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonColorLayoutTxt.setText(R.string.get_start)
        buttonColorLayout.setOnClickListener {
            confirmationListener.clickYes(true)
            dismiss()
        }
    }
    companion object{
        const val TAG = "GetStartBottomSheet"
    }
}