package com.np.namasteyoga.ui.eventModule.eventVideo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.pojo.RegisterResponse
import com.np.namasteyoga.datasource.response.SocialLinkModel
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.EventVideoRepository
import com.np.namasteyoga.repository.SocialLinkRepository
import com.np.namasteyoga.repository.SplashRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.np.namasteyoga.utils.UIUtils

class EventVideoViewModel(
    private val eventVideoRepository: EventVideoRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {





    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int> = _page



    private val _userDetails = MutableLiveData<UserDetail>()
    val userDetails: LiveData<UserDetail>
        get() = _userDetails


    fun getUserDetails() {
        _userDetails.value = SharedPreferencesUtils.getUserDetails(sharedPreferences)
    }

    init {
        _page.value = 0
        getUserDetails()
    }

}