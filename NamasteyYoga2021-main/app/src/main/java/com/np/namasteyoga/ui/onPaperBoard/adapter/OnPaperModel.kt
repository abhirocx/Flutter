package com.np.namasteyoga.ui.onPaperBoard.adapter

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.np.namasteyoga.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OnPaperModel (@DrawableRes val img:Int= R.drawable.art1,val title:String,val msg:String):Parcelable