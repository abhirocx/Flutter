package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.datasource.response.UserDetail
import io.reactivex.Single

interface LoginRepository {
    fun login(email:String?=null,pass:String?=null): Single<BaseResponse<UserDetail>>
    fun resumeAccount(email:String?=null): Single<BaseResponse<UserDetail>>
    fun updateDeviceToken(fcmTokenUpdateModel: FCMTokenUpdateModel): Single<BaseResponseNamasteYoga<EmptyResponse>>
}