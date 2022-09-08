package com.np.namasteyoga.ui.otpVerify

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.pojo.RegisterRequest
import com.np.namasteyoga.datasource.pojo.RegisterResponse
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.datasource.request.LoginRequest
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.OTPVerifyRepository
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils

class OTPVerifyViewModel(
    private val otpVerifyRepository: OTPVerifyRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {



    private val _response = MutableLiveData<BaseResponse<UserDetail>>()
    val response : LiveData<BaseResponse<UserDetail>>
        get() = _response

    private val _responseRegister = MutableLiveData<RegisterResponse>()
    val responseRegister : LiveData<RegisterResponse>
        get() = _responseRegister


    private val _responseOTP = MutableLiveData<BaseResponse<EmptyResponse>>()
    val responseOTP : LiveData<BaseResponse<EmptyResponse>>
        get() = _responseOTP

    fun login(email:String?=null,pass:String?=null){

        launch {
            otpVerifyRepository.login(email,pass)
                .with(scheduler)
                .subscribe({
                    if (it?.status?:CommonString.Empty == C.NP_STATUS_SUCCESS){
                        updateFCMToken(it)
                        return@subscribe
                    }
                    _response.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _response.value = null
                })
        }
    }

    fun register(registerRequest: RegisterRequest) {

        launch {
            otpVerifyRepository.register(registerRequest)
                .with(scheduler)
                .subscribe({
                    _responseRegister.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    _responseRegister.value = null
                })
        }
    }
    fun otpVerify(registerRequest: RegisterRequest,otp:String) {

        launch {
            otpVerifyRepository.verifyOTP(LoginRequest(email = registerRequest.email,otp =otp ))
                .with(scheduler)
                .subscribe({
                    if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS && (registerRequest.roleId != 3 && registerRequest.roleId != 2)){
                        login(registerRequest.email,registerRequest.password)
                        return@subscribe
                    }
                    _responseOTP.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    _responseOTP.value = null
                })
        }
    }

    private fun updateFCMToken(data:BaseResponse<UserDetail>?) {
        val oldDeviceToken = SharedPreferencesUtils.getPushToken(sharedPreferences) ?: CommonString.Empty
        val newDeviceToken = SharedPreferencesUtils.getPushTokenNew(sharedPreferences) ?: CommonString.Empty
        val fcmTokenUpdateModel = FCMTokenUpdateModel(newDeviceToken, oldDeviceToken, data?.data?.id?.toString() ?: CommonString.Empty)

        launch {
            otpVerifyRepository.updateDeviceToken(fcmTokenUpdateModel)
                .with(scheduler)
                .subscribe({
                    if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                        SharedPreferencesUtils.setPushToken(sharedPreferences, newDeviceToken)
                        SharedPreferencesUtils.setPushTokenOnOff(sharedPreferences,CommonInt.One)
                    }
                    _response.value = data
                }, {
                    it?.message?.run(Logger::Error)
                    _response.value = data
                })
        }
    }
}