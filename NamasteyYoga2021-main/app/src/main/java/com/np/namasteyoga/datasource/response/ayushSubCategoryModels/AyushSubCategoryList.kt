package com.np.namasteyoga.datasource.response.ayushSubCategoryModels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AyushSubCategoryList(
    val id: Int? = null,
    val product_description: String? = null,
    val key_ingredients: String? = null,
    val direction: String? = null,
    val product_image: List<ProductImage>? = null,
    val images: List<ProductImage>? = null,
    val product_name: String? = null
):Parcelable