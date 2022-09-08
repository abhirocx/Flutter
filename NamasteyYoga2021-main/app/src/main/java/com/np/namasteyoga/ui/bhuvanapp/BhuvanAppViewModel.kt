package com.np.namasteyoga.ui.bhuvanapp

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.UserListRequest
import com.np.namasteyoga.datasource.request.BhuvanListRequest
import com.np.namasteyoga.datasource.response.BhuvanAppListResponseModel
import com.np.namasteyoga.datasource.response.UserListResponse
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.TrainerListRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils

class BhuvanAppViewModel(
    private val trainerListRepository: TrainerListRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    private val _token = MutableLiveData<String>()
    private val token:LiveData<String> = _token

    private val _response = MutableLiveData<BhuvanAppListResponseModel>()
    val response :LiveData<BhuvanAppListResponseModel> = _response

    private val _page = MutableLiveData<Int>()
    //val page :LiveData<Int> = _page

    fun getListWithPagination(){


        launch {
            val bhuvanListRequest = BhuvanListRequest("all").apply {

                         }
            trainerListRepository.getBhuvanList(bhuvanListRequest,token.value?:CommonString.Empty)
                .with(scheduler)
                .subscribe({
                    _response.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _response.value = null
                })
        }

    }


    init {
        _token.value = SharedPreferencesUtils.getUserDetails(sharedPreferences)?.token?: CommonString.Empty
        _page.value = 0
    }


}