package com.np.namasteyoga.ui.eventModule.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.EventResponse
import com.np.namasteyoga.datasource.pojo.GetEventRequest
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.EventRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils

class PastEventListViewModel(
    private val eventRepository: EventRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    private val _token = MutableLiveData<String>()
    val token:LiveData<String> = _token

    private val _response = MutableLiveData<EventResponse>()
    val response :LiveData<EventResponse> = _response

    val _page = MutableLiveData<Int>()
    val page :LiveData<Int> = _page

    fun getListWithPagination(_city: City){

        _page.value = page.value?.plus(1)
        launch {
            val userListRequest = GetEventRequest().apply {
                page = this@PastEventListViewModel.page.value?:CommonInt.One
                city = _city.city
                state = _city.state_name
                country = _city.country_name
                eventType = CommonString.PAST
            }
            eventRepository.getEventList(userListRequest,token.value?:CommonString.Empty)
                .with(scheduler)
                .subscribe({
                    _response.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _response.value = null
                })
        }

    }

    fun getIsLogin(){
        _token.value = SharedPreferencesUtils.getUserDetails(sharedPreferences)?.token
    }


    init {
        getIsLogin()
        _page.value = 0
    }


}