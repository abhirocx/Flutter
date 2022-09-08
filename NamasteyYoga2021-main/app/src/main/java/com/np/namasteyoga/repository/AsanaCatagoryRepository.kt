package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import io.reactivex.Single


interface AsanaCatagoryRepository {
    fun getAsanaCategory(page:Int): Single<BaseResponse<List<AsanaCategoryModel>>>
}