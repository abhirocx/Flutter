package com.np.namasteyoga.impl

import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.response.CelebrityTestimonialModel
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.CelebrityTestimonialRepository
import io.reactivex.Single

class CelebrityTestimonialImpl(private val restApi: RESTApi):CelebrityTestimonialRepository {
    override fun getCelebrityTestimonialList(page: Int): Single<BaseResponseNamasteYoga<List<CelebrityTestimonialModel>>> {
        return restApi.getCelebrityTestimonialList(page)
    }
}