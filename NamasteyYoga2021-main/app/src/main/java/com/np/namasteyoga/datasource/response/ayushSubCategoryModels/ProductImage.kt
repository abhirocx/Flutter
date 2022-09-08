package com.np.namasteyoga.datasource.response.ayushSubCategoryModels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductImage(
    val image_counter: Int? = null,
    val product_image: String? = null
):Parcelable