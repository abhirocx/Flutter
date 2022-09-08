package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.EmptyResponse
import io.reactivex.Single


interface MyAccountRepository {
    fun suspendAccount(token:String): Single<BaseResponse<EmptyResponse>>
}