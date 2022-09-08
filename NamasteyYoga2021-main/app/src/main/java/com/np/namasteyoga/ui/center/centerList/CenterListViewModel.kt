package com.np.namasteyoga.ui.center.centerList

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.UserListRequest
import com.np.namasteyoga.datasource.response.UserListResponse
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.TrainerListRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils

class CenterListViewModel(
    private val trainerListRepository: TrainerListRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    private val _token = MutableLiveData<String>()
    val token:LiveData<String> = _token

    private val _response = MutableLiveData<UserListResponse>()
    val response :LiveData<UserListResponse> = _response

    private val _page = MutableLiveData<Int>()
    val page :LiveData<Int> = _page

    fun getListWithPagination(_city: City){

        _page.value = page.value?.plus(1)
        launch {
            val userListRequest = UserListRequest().apply {
                page = this@CenterListViewModel.page.value?:CommonInt.One
                city = _city.city
                state = _city.state_name
                country = _city.country_name
                roleId = CommonInt.Two
            }
            trainerListRepository.getUserList(userListRequest,token.value?:CommonString.Empty)
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