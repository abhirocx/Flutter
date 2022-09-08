package com.np.namasteyoga.ui.splash

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.SplashRepository
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.np.namasteyoga.utils.UIUtils

class SplashViewModel(
    private val splashRepository: SplashRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    val response = MutableLiveData<BaseResponse<VersionData>>()

    fun getVersion() {

        launch {
            splashRepository.getVersion()
                .with(scheduler)
                .subscribe({
                    response.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    response.value = null
                })
        }
    }

    val responseR = MutableLiveData<BaseResponseNamasteYoga<EmptyResponse>>()

    fun updateFCMToken() {
        val oldDeviceToken =
            SharedPreferencesUtils.getPushToken(sharedPreferences) ?: CommonString.Empty
        val newDeviceToken =
            SharedPreferencesUtils.getPushTokenNew(sharedPreferences) ?: CommonString.Empty
        val fcmTokenUpdateModel =
            FCMTokenUpdateModel(newDeviceToken, oldDeviceToken, userDetail.value?.id?.toString() ?: CommonString.Empty,SharedPreferencesUtils.getPushTokenOnOff(sharedPreferences))

        Logger.Debug(fcmTokenUpdateModel.toString())
        if (oldDeviceToken == newDeviceToken )
            return
        launch {
            splashRepository.updateDeviceToken(fcmTokenUpdateModel)
                .with(scheduler)
                .subscribe({
                    Logger.Debug(it.toString())
                    if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS)
                        SharedPreferencesUtils.setPushToken(sharedPreferences, newDeviceToken)
                    responseR.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    responseR.value = null
                })
        }
    }
    private val _userDetail = MutableLiveData<UserDetail>()
    val userDetail: LiveData<UserDetail> = _userDetail

    fun getUserDetails() {
        _userDetail.value = SharedPreferencesUtils.getUserDetails(sharedPreferences)
    }

    init {
        UIUtils.setToken(sharedPreferences)
        getUserDetails()
    }


}