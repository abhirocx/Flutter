package com.np.namasteyoga.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialogFragment
import com.np.namasteyoga.R

class MyRoundedBottomSheet: RoundedBottomSheetDialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.center_details_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}