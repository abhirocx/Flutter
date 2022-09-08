package com.np.namasteyoga.impl

import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.response.AyushMerchandiseModel
import com.np.namasteyoga.datasource.response.ayushSubCategoryModels.AyushSubCategoryList
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.AyushCatagoryRepository
import io.reactivex.Single

class AyushCatagoryImpl (private val restApi: RESTApi):AyushCatagoryRepository{
    override fun getAyushCategoryList(page: Int): Single<BaseResponseNamasteYoga<List<AyushMerchandiseModel>>> {
        return restApi.getAyushCategoryList(page)
    }

    override fun getAyushSubCategoryList(
        page: Int,
        category_id: String
    ): Single<BaseResponseNamasteYoga<List<AyushSubCategoryList>>> {
        return restApi.getAyushSubCategoryList(page, category_id)
    }

    override fun getAyushProductDetails(product_id: String): Single<BaseResponseNamasteYoga<AyushSubCategoryList>> {
        return restApi.getAyushProductDetails(product_id)
    }


}