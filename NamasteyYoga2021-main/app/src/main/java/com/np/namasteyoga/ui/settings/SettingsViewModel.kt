package com.np.namasteyoga.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.SettingsRepository
import com.np.namasteyoga.repository.SplashRepository
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.np.namasteyoga.utils.UIUtils

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    private val _response = MutableLiveData<BaseResponse<EmptyResponse>>()
    val response: LiveData<BaseResponse<EmptyResponse>> = _response

    fun logout() {

        launch {
            settingsRepository.logout(token.value ?: CommonString.Empty)
                .with(scheduler)
                .subscribe({
                    _response.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    _response.value = null
                })
        }
    }

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    fun tokenUpdate() {
        _token.value = SharedPreferencesUtils.getUserDetails(sharedPreferences)?.token
    }

    val responseR = MutableLiveData<BaseResponseNamasteYoga<EmptyResponse>>()

    fun updateFCMToken() {
        val oldDeviceToken =
            SharedPreferencesUtils.getPushToken(sharedPreferences) ?: CommonString.Empty
        val newDeviceToken =
            SharedPreferencesUtils.getPushTokenNew(sharedPreferences) ?: CommonString.Empty

        val settings = SharedPreferencesUtils.getPushTokenOnOff(sharedPreferences)
        val updateSettings = if (settings == CommonInt.Zero){
            CommonInt.One
        } else
        {
            CommonInt.Zero
        }
        val fcmTokenUpdateModel = FCMTokenUpdateModel(
            newDeviceToken,
            oldDeviceToken,
            userDetail.value?.id?.toString() ?: CommonString.Empty,
            updateSettings
        )
        launch {
            settingsRepository.updateDeviceToken(fcmTokenUpdateModel)
                .with(scheduler)
                .subscribe({
                    if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                        SharedPreferencesUtils.setPushToken(sharedPreferences, newDeviceToken)
                        SharedPreferencesUtils.setPushTokenOnOff(sharedPreferences, updateSettings)
                    }
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