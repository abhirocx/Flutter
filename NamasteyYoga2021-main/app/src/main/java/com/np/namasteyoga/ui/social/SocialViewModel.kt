package com.np.namasteyoga.ui.social

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
import com.np.namasteyoga.repository.SocialLinkRepository
import com.np.namasteyoga.repository.SplashRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.np.namasteyoga.utils.UIUtils

class SocialViewModel(
    private val socialLinkRepository: SocialLinkRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {


    private val _response = MutableLiveData<BaseResponse<List<SocialLinkModel>>>()
    val response: LiveData<BaseResponse<List<SocialLinkModel>>>
        get() = _response


    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int> = _page

    fun getSocialLinks() {
        _page.value = page.value?.plus(1)
        launch {
            socialLinkRepository.getSocialLinks(page.value?: CommonInt.One)
                .with(scheduler)
                .subscribe({
                    _response.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _response.value = null
                })
        }
    }

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