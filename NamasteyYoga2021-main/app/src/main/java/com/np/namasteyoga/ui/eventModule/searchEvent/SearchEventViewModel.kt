package com.np.namasteyoga.ui.eventModule.searchEvent

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.Event
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.SearchEventRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils

class SearchEventViewModel(
    private val searchEventRepository: SearchEventRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {



    private val _userDetails = MutableLiveData<UserDetail>()
    val userDetails : LiveData<UserDetail>
        get() = _userDetails

    private val _response = MutableLiveData<BaseResponseNamasteYoga<List<Event>>>()

    val response : LiveData<BaseResponseNamasteYoga<List<Event>>>
        get() = _response


    fun searchEvent(search:String,city: City){
        launch {
            searchEventRepository.searchEventList(search,city)
                .with(scheduler)
                .subscribe({
                    _response.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _response.value = null
                })
        }
    }

    fun getUserDetails(){
        _userDetails.value = SharedPreferencesUtils.getUserDetails(sharedPreferences)
    }
    init {
        getUserDetails()
    }

}