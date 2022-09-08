package com.np.namasteyoga.impl

import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.TestFCMRepository
import io.reactivex.Single

class TestFCMImpl(private val restApi: RESTApi):TestFCMRepository {


    override fun updateDeviceToken(fcmTokenUpdateModel: FCMTokenUpdateModel): Single<BaseResponseNamasteYoga<EmptyResponse>> {
        return try {

            restApi.updateDeviceToken(fcmTokenUpdateModel.user_id?:CommonString.Empty,fcmTokenUpdateModel.new_device_id?:CommonString.Empty,fcmTokenUpdateModel.old_device_id?:CommonString.Empty,fcmTokenUpdateModel.notificationSetting)
        }catch (e:Exception){
            Single.error(Throwable(e))
        }
    }
}