package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.pojo.EventResponse
import com.np.namasteyoga.datasource.pojo.GetEventRequest
import io.reactivex.Single


interface EventRepository {

    fun getEventList(getEventRequest: GetEventRequest,token:String?): Single<EventResponse>
}