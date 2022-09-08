package com.np.namasteyoga.impl

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import com.np.namasteyoga.datasource.response.AyushMerchandiseModel
import com.np.namasteyoga.datasource.response.CelebrityTestimonialModel
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.MainRepository
import io.reactivex.Single

class MainImpl (private val restApi: RESTApi):MainRepository{
    override fun getAsanaCategory(page: Int): Single<BaseResponse<List<AsanaCategoryModel>>> = restApi.getAsanaCategory(page)
    override fun getAyushCategoryList(page: Int): Single<BaseResponseNamasteYoga<List<AyushMerchandiseModel>>> {
        return restApi.getAyushCategoryList(page)
    }

    override fun getCelebrityTestimonialList(page: Int): Single<BaseResponseNamasteYoga<List<CelebrityTestimonialModel>>> {
        return restApi.getCelebrityTestimonialList(page)
    }

}