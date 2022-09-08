package com.np.namasteyoga.ui.joiningInstructions

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.pojo.RegisterResponse
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.JoiningInstructionRepository
import com.np.namasteyoga.repository.MyAccountRepository
import com.np.namasteyoga.repository.SplashRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.np.namasteyoga.utils.UIUtils

class JoiningInstructionsViewModel(
    private val joiningInstructionRepository: JoiningInstructionRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {



    private val _userDetails = MutableLiveData<UserDetail>()
    val userDetails : LiveData<UserDetail>
        get() = _userDetails

    private val _response = MutableLiveData<BaseResponseNamasteYoga<EmptyResponse>>()

    val response : LiveData<BaseResponseNamasteYoga<EmptyResponse>>
        get() = _response


    fun interested(eventId:String){
        launch {
            joiningInstructionRepository.interested(userDetails.value?.token?:CommonString.Empty,eventId)
                .with(scheduler)
                .subscribe({
                    _response.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _response.value = null
                })
        }
    }

    fun getUserDetails(){
        _userDetails.value = SharedPreferencesUtils.getUserDetails(sharedPreferences)
    }
    init {
        getUserDetails()
    }

}