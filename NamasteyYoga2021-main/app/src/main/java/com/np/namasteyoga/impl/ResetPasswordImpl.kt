package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.LoginRequest
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.ForgotPasswordRepository
import com.np.namasteyoga.repository.RegisterRepository
import com.np.namasteyoga.repository.ResetPasswordRepository
import com.np.namasteyoga.utils.ConstUtility
import com.np.namasteyoga.utils.Constants
import io.reactivex.Single

class ResetPasswordImpl(private val restApi: RESTApi) : ResetPasswordRepository {
    override fun forgotPassword(email: String?): Single<BaseResponse<EmptyResponse>> {
        if (email == null) {
            return Single.error(Throwable(CommonString.DATA_NULL))
        }
        return try {
            val _email = ConstUtility.encryptLC(email)
            val loginRequest = LoginRequest(_email)
            val json = Gson().toJson(loginRequest)
            val header = Constants.getMD5EncryptedString(json)
            restApi.forgotPassword(loginRequest, header)
        } catch (e: Exception) {
            Single.error(Throwable(e))
        }
    }

    override fun verifyPassword(login: LoginRequest?): Single<BaseResponse<EmptyResponse>> {
        if (login?.email == null || login.newpass == null || login.otp == null) {
            return Single.error(Throwable(CommonString.DATA_NULL))
        }
        return try {
            val _email = ConstUtility.encryptLC(login.email)
            val _newpass = ConstUtility.encryptLC(login.newpass)
            val _otp = ConstUtility.encryptLC(login.otp)
            val loginRequest = LoginRequest(_email,newpass = _newpass,otp = _otp)
            val json = Gson().toJson(loginRequest)
            val header = Constants.getMD5EncryptedString(json)
            restApi.verifyPassword(loginRequest, header)
        } catch (e: Exception) {
            Single.error(Throwable(e))
        }
    }
}