package com.np.namasteyoga.ui.trainer.searchtrainer

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.UserListRequest
import com.np.namasteyoga.datasource.response.UserListResponse
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.TrainerSearchListRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils

class TrainerSearchListViewModel(
    private val trainerListRepository: TrainerSearchListRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    private val _token = MutableLiveData<String>()
    private val token: LiveData<String> = _token

    private val _response = MutableLiveData<UserListResponse>()
    val response: LiveData<UserListResponse> = _response

    fun getListWithPagination(_city: City, key: String) {

        launch {
            val userListRequest = UserListRequest().apply {
                city = _city.city
                state = _city.state_name
                country = _city.country_name
                search = key
                type = "trainer"
            }
            Logger.Debug(userListRequest.toString())
            trainerListRepository.getUserList(userListRequest, token.value ?: CommonString.Empty)
                .with(scheduler)
                .subscribe({
                    _response.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    _response.value = null
                })
        }

    }


    init {
        _token.value =
            SharedPreferencesUtils.getUserDetails(sharedPreferences)?.token ?: CommonString.Empty
    }


}