package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import io.reactivex.Single


interface JoiningInstructionRepository {
    fun interested(token:String,eventId:String): Single<BaseResponseNamasteYoga<EmptyResponse>>
}