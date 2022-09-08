package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.LoginRequest
import io.reactivex.Single

interface ForgotPasswordRepository {

    fun forgotPassword(email:String?=null): Single<BaseResponse<EmptyResponse>>

}