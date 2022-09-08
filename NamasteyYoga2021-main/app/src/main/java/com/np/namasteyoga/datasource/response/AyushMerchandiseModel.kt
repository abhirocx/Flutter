package com.np.namasteyoga.datasource.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AyushMerchandiseModel(
    val category_description: String? = null,
    val category_name: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val id: Int? = null,
    val image: String? = null,
    val status: String? = null,
    val updated_at: String? = null
):Parcelable