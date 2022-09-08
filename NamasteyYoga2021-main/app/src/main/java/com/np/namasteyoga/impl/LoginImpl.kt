package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.CommonString.DATA_NULL
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.datasource.request.LoginRequest
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.LoginRepository
import com.np.namasteyoga.utils.ConstUtility
import com.np.namasteyoga.utils.Constants
import io.reactivex.Single

class LoginImpl(private val restApi: RESTApi) : LoginRepository {
    override fun login(email: String?, pass: String?): Single<BaseResponse<UserDetail>> {
        if (email == null || pass == null) {
            return Single.error(Throwable(DATA_NULL))
        }
        return try {
            val _email = ConstUtility.encryptLC(email)
            val _pass = ConstUtility.encryptLC(pass)
            val loginRequest = LoginRequest(_email, _pass)
            val json = Gson().toJson(loginRequest)
            val header = Constants.getMD5EncryptedString(json)
            restApi.login(loginRequest, header)
        } catch (e: Exception) {
            Single.error(Throwable(e))
        }
    }

    override fun resumeAccount(email: String?): Single<BaseResponse<UserDetail>> {
        if (email == null ) {
            return Single.error(Throwable(DATA_NULL))
        }
        return try {
            val _email = ConstUtility.encryptLC(email)
            val loginRequest = LoginRequest(_email)
            val json = Gson().toJson(loginRequest)
            val header = Constants.getMD5EncryptedString(json)
            restApi.resumeAccount(loginRequest, header)
        } catch (e: Exception) {
            Single.error(Throwable(e))
        }
    }

    override fun updateDeviceToken(fcmTokenUpdateModel: FCMTokenUpdateModel): Single<BaseResponseNamasteYoga<EmptyResponse>> {
        return try {

            restApi.updateDeviceToken(fcmTokenUpdateModel.user_id?: CommonString.Empty,fcmTokenUpdateModel.new_device_id?: CommonString.Empty,fcmTokenUpdateModel.old_device_id?: CommonString.Empty,fcmTokenUpdateModel.notificationSetting)
        }catch (e:Exception){
            Single.error(Throwable(e))
        }
    }
}