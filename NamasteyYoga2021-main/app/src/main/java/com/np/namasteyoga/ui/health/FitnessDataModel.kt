package com.np.namasteyoga.ui.health

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FitnessDataModel(
    val month: String = "",
    val stat: String? = null
):Parcelable