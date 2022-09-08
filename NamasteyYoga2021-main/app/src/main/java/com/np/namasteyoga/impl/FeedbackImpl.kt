package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.FeedbackSubmitRequest
import com.np.namasteyoga.datasource.response.FeedbackResponseModel
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.FeedbackRepository
import com.np.namasteyoga.repository.MainRepository
import com.np.namasteyoga.utils.Constants
import io.reactivex.Single

class FeedbackImpl (private val restApi: RESTApi):FeedbackRepository{
    override fun getFeedbackList(page: Int, token: String): Single<FeedbackResponseModel> {

        return try {
            val json = Gson().toJson(EmptyResponse())
            val header = Constants.getMD5EncryptedStringWithToken(json,token)
            restApi.getFeedbackList(page,header)
        }catch (e:Exception){
            Single.error(Throwable(e))
        }
    }

    override fun submitFeedback(
        feedbackSubmitRequest: FeedbackSubmitRequest,
        token: String
    ): Single<BaseResponseNamasteYoga<EmptyResponse>> {
        return try {
            val json = Gson().toJson(feedbackSubmitRequest)
            val header = Constants.getMD5EncryptedStringWithToken(json,token)
            restApi.submitFeedback(feedbackSubmitRequest,header)
        }catch (e:Exception){
            Single.error(Throwable(e))
        }
    }

}