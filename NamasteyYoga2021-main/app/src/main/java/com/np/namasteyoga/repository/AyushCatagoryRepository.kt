package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.response.AyushMerchandiseModel
import com.np.namasteyoga.datasource.response.ayushSubCategoryModels.AyushSubCategoryList
import io.reactivex.Single


interface AyushCatagoryRepository {
    fun getAyushCategoryList(page:Int):Single<BaseResponseNamasteYoga<List<AyushMerchandiseModel>>>
    fun getAyushSubCategoryList(page:Int,category_id: String):Single<BaseResponseNamasteYoga<List<AyushSubCategoryList>>>
    fun getAyushProductDetails(product_id: String):Single<BaseResponseNamasteYoga<AyushSubCategoryList>>
}