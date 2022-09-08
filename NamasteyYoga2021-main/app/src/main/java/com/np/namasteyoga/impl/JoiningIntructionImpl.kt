package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.JoiningInstructionRepository
import com.np.namasteyoga.utils.Constants
import io.reactivex.Single

class JoiningIntructionImpl(private val restApi: RESTApi):JoiningInstructionRepository {
    override fun interested(token: String, eventId: String): Single<BaseResponseNamasteYoga<EmptyResponse>> {
        return try {
            val json = Gson().toJson(EmptyResponse())
            val header = Constants.getMD5EncryptedStringWithToken(json,token)
            restApi.interested(eventId,header)
        }catch (e:Exception){
            Single.error(Throwable(e))
        }
    }
}