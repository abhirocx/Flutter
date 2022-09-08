package com.np.namasteyoga.ui.updateAccount.trainer

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.pojo.EditRequest
import com.np.namasteyoga.datasource.request.FCMTokenUpdateModel
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.AccountUpdateRepository
import com.np.namasteyoga.repository.SplashRepository
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.np.namasteyoga.utils.UIUtils

class TrainerAccountUpdateViewModel(
    private val updateRepository: AccountUpdateRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    private val _response = MutableLiveData<BaseResponse<UserDetail>>()
    val response: LiveData<BaseResponse<UserDetail>> = _response


    private val _userDetail = MutableLiveData<UserDetail>()
    val userDetail: LiveData<UserDetail> = _userDetail

    fun getUserDetails() {
        _userDetail.value = SharedPreferencesUtils.getUserDetails(sharedPreferences)
    }

    fun updateAccount(editRequest: EditRequest) {
        launch {
            updateRepository.updateAccount(
                editRequest,
                userDetail.value?.token ?: CommonString.Empty
            )
                .with(scheduler)
                .subscribe({
                    _response.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    _response.value = null
                })
        }
    }

    init {
        UIUtils.setToken(sharedPreferences)
        getUserDetails()
    }


}