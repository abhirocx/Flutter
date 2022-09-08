package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.pojo.RegisterRequest
import com.np.namasteyoga.datasource.pojo.RegisterResponse
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.datasource.request.LoginRequest
import com.np.namasteyoga.datasource.response.UserDetail
import io.reactivex.Single

interface OTPVerifyRepository {
    fun login(email:String?,pass:String?): Single<BaseResponse<UserDetail>>
    fun register(registerRequest: RegisterRequest?): Single<RegisterResponse>
    fun verifyOTP(loginRequest: LoginRequest?): Single<BaseResponse<EmptyResponse>>
    fun updateDeviceToken(fcmTokenUpdateModel: FCMTokenUpdateModel): Single<BaseResponseNamasteYoga<EmptyResponse>>
}