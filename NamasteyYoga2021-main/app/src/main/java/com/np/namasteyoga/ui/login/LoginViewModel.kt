package com.np.namasteyoga.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.common.STATUS_WITH_MSG
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.LoginRepository
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.np.namasteyoga.utils.UIUtils

class LoginViewModel(
    private val loginRepository: LoginRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {


    private val _response = MutableLiveData<BaseResponse<UserDetail>>()
    val response :LiveData<BaseResponse<UserDetail>>
    get() = _response

    private val _responseResume = MutableLiveData<BaseResponse<UserDetail>>()
    val responseResume :LiveData<BaseResponse<UserDetail>>
        get() = _responseResume

    private val _status = MutableLiveData<STATUS_WITH_MSG>()
    val status: LiveData<STATUS_WITH_MSG>
        get() = _status

    fun login(email:String?=null,pass:String?=null){
        launch {
            loginRepository.login(email?.toLowerCase(),pass)
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
    fun resumeAccount(email:String?=null){
        launch {
            loginRepository.resumeAccount(email)
                .with(scheduler)
                .subscribe({
                    _responseResume.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _responseResume.value = null
                })
        }
    }


    private fun updateFCMToken(data:BaseResponse<UserDetail>?) {
        val oldDeviceToken = SharedPreferencesUtils.getPushToken(sharedPreferences) ?: CommonString.Empty
        val newDeviceToken = SharedPreferencesUtils.getPushTokenNew(sharedPreferences) ?: CommonString.Empty
        val fcmTokenUpdateModel = FCMTokenUpdateModel(newDeviceToken, oldDeviceToken, data?.data?.id?.toString() ?: CommonString.Empty)

        launch {
            loginRepository.updateDeviceToken(fcmTokenUpdateModel)
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

    init {
        UIUtils.setToken(sharedPreferences)
    }

}