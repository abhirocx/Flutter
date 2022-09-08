package com.np.namasteyoga.impl

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.AsanaCatagoryRepository
import io.reactivex.Single

class AsanaCatagoryImpl (private val restApi: RESTApi):AsanaCatagoryRepository{
    override fun getAsanaCategory(page: Int): Single<BaseResponse<List<AsanaCategoryModel>>> = restApi.getAsanaCategory(page)

}