package com.np.namasteyoga.impl

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.MainRepository
import com.np.namasteyoga.repository.MyAccountRepository
import com.np.namasteyoga.utils.Util
import io.reactivex.Single

class MyAccountImpl (private val restApi: RESTApi):MyAccountRepository{
    override fun suspendAccount(token: String): Single<BaseResponse<EmptyResponse>> {
        return restApi.suspendAccount(Util.getHeaderPHP(token))
    }

}