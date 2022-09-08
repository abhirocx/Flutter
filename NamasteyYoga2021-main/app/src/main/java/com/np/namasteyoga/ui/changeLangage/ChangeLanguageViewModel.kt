package com.np.namasteyoga.ui.changeLangage

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.common.STATUS_WITH_MSG
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.LoginRepository
import com.np.namasteyoga.utils.Logger

class ChangeLanguageViewModel(
    private val loginRepository: LoginRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {


    private val _response = MutableLiveData<BaseResponse<UserDetail>>()
    val response :LiveData<BaseResponse<UserDetail>>
    get() = _response

    private val _status = MutableLiveData<STATUS_WITH_MSG>()
    val status: LiveData<STATUS_WITH_MSG>
        get() = _status

    fun login(email:String?=null,pass:String?=null){
        launch {
            loginRepository.login(email,pass)
                .with(scheduler)
                .subscribe({
                    _response.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _response.value = null
                })
        }
    }



}