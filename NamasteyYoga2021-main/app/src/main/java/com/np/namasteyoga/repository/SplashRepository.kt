package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.datasource.request.GetVersionRequest
import io.reactivex.Single

interface SplashRepository {

    fun getVersion(getVersionRequest: GetVersionRequest= GetVersionRequest()): Single<BaseResponse<VersionData>>
    fun updateDeviceToken(fcmTokenUpdateModel: FCMTokenUpdateModel): Single<BaseResponseNamasteYoga<EmptyResponse>>
}