package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.datasource.request.GetVersionRequest
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.SplashRepository
import com.np.namasteyoga.utils.Constants
import io.reactivex.Single

class SplashImpl(private val restApi: RESTApi):SplashRepository {
    override fun getVersion(getVersionRequest: GetVersionRequest): Single<BaseResponse<VersionData>> {
        return try {
            val json = Gson().toJson(getVersionRequest)
            val header = Constants.getMD5EncryptedString(json)
            restApi.getVersion(getVersionRequest, header)
        }catch (e:Exception){
            Single.error(Throwable(e))
        }
    }
    override fun updateDeviceToken(fcmTokenUpdateModel: FCMTokenUpdateModel): Single<BaseResponseNamasteYoga<EmptyResponse>> {
        return try {

            restApi.updateDeviceToken(fcmTokenUpdateModel.user_id?:CommonString.Empty,fcmTokenUpdateModel.new_device_id?:CommonString.Empty,fcmTokenUpdateModel.old_device_id?:CommonString.Empty,fcmTokenUpdateModel.notificationSetting)
        }catch (e:Exception){
            Single.error(Throwable(e))
        }
    }
}