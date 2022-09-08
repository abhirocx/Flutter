package com.np.namasteyoga.ui.fcmTestActivity

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.TestFCMRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.np.namasteyoga.utils.UIUtils

class TestFCMViewModel(
    private val testFCMRepository: TestFCMRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    val response = MutableLiveData<BaseResponseNamasteYoga<EmptyResponse>>()

    fun updateFCMToken(id:String?=null) {
        val oldDeviceToken =
            SharedPreferencesUtils.getPushToken(sharedPreferences) ?: CommonString.Empty
        val newDeviceToken =
            SharedPreferencesUtils.getPushTokenNew(sharedPreferences) ?: CommonString.Empty
        val fcmTokenUpdateModel = FCMTokenUpdateModel(newDeviceToken,oldDeviceToken,id?:CommonString.Empty)
        launch {
            testFCMRepository.updateDeviceToken(fcmTokenUpdateModel)
                .with(scheduler)
                .subscribe({
                    SharedPreferencesUtils.setPushToken(sharedPreferences,newDeviceToken)
                    response.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    response.value = null
                })
        }
//        SharedPreferencesUtils.getUserDetails(sharedPreferences)?.id?.run {
//            val fcmTokenUpdateModel = FCMTokenUpdateModel(newDeviceToken,oldDeviceToken,this.toString())
//            launch {
//                testFCMRepository.updateDeviceToken(fcmTokenUpdateModel)
//                    .with(scheduler)
//                    .subscribe({
//                        SharedPreferencesUtils.setPushToken(sharedPreferences,newDeviceToken)
//                        response.value = it
//                    }, {
//                        it?.message?.run(Logger::Error)
//                        response.value = null
//                    })
//            }
//        }
    }

    init {
        UIUtils.setToken(sharedPreferences)
    }


}