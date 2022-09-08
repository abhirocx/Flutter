package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import com.np.namasteyoga.datasource.response.AyushMerchandiseModel
import com.np.namasteyoga.datasource.response.CelebrityTestimonialModel
import io.reactivex.Single


interface MainRepository {
    fun getAsanaCategory(page:Int): Single<BaseResponse<List<AsanaCategoryModel>>>
    fun getAyushCategoryList(page:Int):Single<BaseResponseNamasteYoga<List<AyushMerchandiseModel>>>
    fun getCelebrityTestimonialList(page:Int):Single<BaseResponseNamasteYoga<List<CelebrityTestimonialModel>>>
}