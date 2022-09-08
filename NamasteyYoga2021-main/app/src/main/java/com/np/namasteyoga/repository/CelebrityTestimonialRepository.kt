package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.response.AyushMerchandiseModel
import com.np.namasteyoga.datasource.response.CelebrityTestimonialModel
import com.np.namasteyoga.datasource.response.ayushSubCategoryModels.AyushSubCategoryList
import io.reactivex.Single


interface CelebrityTestimonialRepository {
    fun getCelebrityTestimonialList(page:Int):Single<BaseResponseNamasteYoga<List<CelebrityTestimonialModel>>>
}