package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import io.reactivex.Single

interface TestFCMRepository {

    fun updateDeviceToken(fcmTokenUpdateModel: FCMTokenUpdateModel): Single<BaseResponseNamasteYoga<EmptyResponse>>
}