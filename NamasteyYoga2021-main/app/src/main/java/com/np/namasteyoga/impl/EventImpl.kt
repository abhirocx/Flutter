package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.datasource.pojo.EventResponse
import com.np.namasteyoga.datasource.pojo.GetEventRequest
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.EventRepository
import com.np.namasteyoga.repository.MainRepository
import com.np.namasteyoga.utils.Constants
import io.reactivex.Single

class EventImpl(private val restApi: RESTApi) : EventRepository {
    override fun getEventList(
        getEventRequest: GetEventRequest,
        token: String?
    ): Single<EventResponse> {
        return try {
            val json = Gson().toJson(getEventRequest)
            val header = if (token == null) {
                Constants.getMD5EncryptedString(json)
            } else
                Constants.getMD5EncryptedStringWithToken(json, token)
            restApi.getEventList(getEventRequest, header)
        } catch (e: Exception) {
            Single.error(Throwable(e))
        }
    }

}