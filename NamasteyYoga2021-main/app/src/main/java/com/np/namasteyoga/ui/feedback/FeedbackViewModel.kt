package com.np.namasteyoga.ui.feedback

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
import com.np.namasteyoga.datasource.request.FeedbackSubmitRequest
import com.np.namasteyoga.datasource.response.FeedbackResponseModel
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.FeedbackRepository
import com.np.namasteyoga.repository.SplashRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.np.namasteyoga.utils.UIUtils

class FeedbackViewModel(
    private val feedbackRepository: FeedbackRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {



    private val _userDetails = MutableLiveData<UserDetail>()
    val userDetails : LiveData<UserDetail>
        get() = _userDetails

    private val _page = MutableLiveData<Int>()
    val page :LiveData<Int> = _page

    private val _response = MutableLiveData<FeedbackResponseModel>()
    val response: LiveData<FeedbackResponseModel> = _response

    private val _response2 = MutableLiveData<BaseResponseNamasteYoga<EmptyResponse>>()
    val response2: LiveData<BaseResponseNamasteYoga<EmptyResponse>> = _response2


    fun getFeedbackList(){
        launch {
            _page.value = page.value?.plus(1)
            feedbackRepository.getFeedbackList(page.value?:1,userDetails.value?.token?:CommonString.Empty)
                .with(scheduler)
                .subscribe({
                    _response.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    _response.value = null
                })
        }
    }

    fun submitFeedback(feedbackSubmitRequest: FeedbackSubmitRequest) {
        launch {

            feedbackRepository.submitFeedback(
                feedbackSubmitRequest,
                userDetails.value?.token ?: CommonString.Empty
            )
                .with(scheduler)
                .subscribe({
                    _response2.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    _response2.value = null
                })
        }
    }

    fun getUserDetails(){
        _userDetails.value = SharedPreferencesUtils.getUserDetails(sharedPreferences)
    }
    init {
        getUserDetails()
        _page.value = 0
    }

}