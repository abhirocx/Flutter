package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.pojo.RegisterRequest
import com.np.namasteyoga.datasource.pojo.RegisterResponse
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.datasource.request.LoginRequest
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.OTPVerifyRepository
import com.np.namasteyoga.utils.ConstUtility
import com.np.namasteyoga.utils.Constants
import io.reactivex.Single

class OTPVerifyImpl(private val restApi: RESTApi):OTPVerifyRepository {
    override fun login(email: String?, pass: String?): Single<BaseResponse<UserDetail>> {
        if (email == null || pass == null) {
            return Single.error(Throwable(CommonString.DATA_NULL))
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

    override fun register(registerRequest: RegisterRequest?): Single<RegisterResponse> {
        if (registerRequest?.password == null || registerRequest.phone == null || registerRequest.email == null) {
            return Single.error(Throwable(CommonString.DATA_NULL))
        }
        return try {
            val _email = ConstUtility.encryptLC(registerRequest.email)
            val _newpass = ConstUtility.encryptLC(registerRequest.password)
            val _phone = ConstUtility.encryptLC(registerRequest.phone)

            val register = clone(data = registerRequest)
            register.run {
                email = _email
                password = _newpass
                passwordConfirmation = _newpass
                phone = _phone
            }


            val json = Gson().toJson(register)
            val header = Constants.getMD5EncryptedString(json)
            restApi.register(register, header)
        } catch (e: Exception) {
            Single.error(Throwable(e))
        }
    }

    override fun verifyOTP(loginRequest: LoginRequest?): Single<BaseResponse<EmptyResponse>> {
        if (loginRequest?.email == null || loginRequest.otp == null) {
            return Single.error(Throwable(CommonString.DATA_NULL))
        }
        return try {
            val _email = ConstUtility.encryptLC(loginRequest.email)
            val _otp = ConstUtility.encryptLC(loginRequest.otp)
            loginRequest.otp = _otp
            loginRequest.email = _email
            val json = Gson().toJson(loginRequest)
            val header = Constants.getMD5EncryptedString(json)
            restApi.otpVerify(loginRequest, header)
        } catch (e: Exception) {
            Single.error(Throwable(e))
        }
    }

    private fun clone(data: RegisterRequest): RegisterRequest {
        val stringAnimal = Gson().toJson(data, RegisterRequest::class.java)
        return Gson().fromJson(stringAnimal, RegisterRequest::class.java)
    }
    override fun updateDeviceToken(fcmTokenUpdateModel: FCMTokenUpdateModel): Single<BaseResponseNamasteYoga<EmptyResponse>> {
        return try {

            restApi.updateDeviceToken(fcmTokenUpdateModel.user_id?: CommonString.Empty,fcmTokenUpdateModel.new_device_id?: CommonString.Empty,fcmTokenUpdateModel.old_device_id?: CommonString.Empty,fcmTokenUpdateModel.notificationSetting)
        }catch (e:Exception){
            Single.error(Throwable(e))
        }
    }
}