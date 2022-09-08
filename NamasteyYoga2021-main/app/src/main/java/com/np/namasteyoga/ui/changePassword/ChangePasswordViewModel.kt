package com.np.namasteyoga.ui.changePassword

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.ChangePasswordModel
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.ChangePasswordRepository
import com.np.namasteyoga.repository.ForgotPasswordRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils

class ChangePasswordViewModel(
    private val changePasswordRepository: ChangePasswordRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {


    private val _token = MutableLiveData<String>()
    private val token:LiveData<String> = _token

    private val _changePassword = MutableLiveData<BaseResponse<EmptyResponse>>()
    val changePassword : LiveData<BaseResponse<EmptyResponse>>
        get() = _changePassword


    fun changePassword(oldPass:String?=null,newPass:String?=null){

        if (token.value == null){
            _changePassword.value = null
            return
        }
        launch {
            changePasswordRepository.changePassword(ChangePasswordModel(newPass,newPass,oldPass),token.value?:CommonString.Empty)
                .with(scheduler)
                .subscribe({
                    _changePassword.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _changePassword.value = null
                })
        }
    }

    init {
      _token.value = SharedPreferencesUtils.getUserDetails(sharedPreferences)?.token?:CommonString.Empty
    }

}