package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.FeedbackSubmitRequest
import com.np.namasteyoga.datasource.response.FeedbackResponseModel
import io.reactivex.Single


interface FeedbackRepository {
    fun getFeedbackList(page: Int,token:String ): Single<FeedbackResponseModel>
    fun submitFeedback(feedbackSubmitRequest: FeedbackSubmitRequest,token: String):Single<BaseResponseNamasteYoga<EmptyResponse>>
}