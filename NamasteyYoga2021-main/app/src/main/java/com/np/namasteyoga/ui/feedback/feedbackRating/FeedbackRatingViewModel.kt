package com.np.namasteyoga.ui.feedback.feedbackRating

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.FeedbackSubmitRequest
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.FeedbackRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils

class FeedbackRatingViewModel(
    private val feedbackRepository: FeedbackRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {


    private val _userDetails = MutableLiveData<UserDetail>()
    val userDetails: LiveData<UserDetail>
        get() = _userDetails


    private val _response = MutableLiveData<BaseResponseNamasteYoga<EmptyResponse>>()
    val response: LiveData<BaseResponseNamasteYoga<EmptyResponse>> = _response


    fun submitFeedback(feedbackSubmitRequest: FeedbackSubmitRequest) {
        launch {

            feedbackRepository.submitFeedback(
                feedbackSubmitRequest,
                userDetails.value?.token ?: CommonString.Empty
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

    fun getUserDetails() {
        _userDetails.value = SharedPreferencesUtils.getUserDetails(sharedPreferences)
    }

    init {
        getUserDetails()
    }

}