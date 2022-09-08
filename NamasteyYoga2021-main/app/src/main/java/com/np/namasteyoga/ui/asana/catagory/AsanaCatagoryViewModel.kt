package com.np.namasteyoga.ui.asana.catagory

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.UserListRequest
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import com.np.namasteyoga.datasource.response.UserListResponse
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.AsanaCatagoryRepository
import com.np.namasteyoga.repository.TrainerListRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils

class AsanaCatagoryViewModel(
    private val asanaCatagoryRepository: AsanaCatagoryRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    private val _token = MutableLiveData<String>()
    private val token: LiveData<String> = _token

    private val _response = MutableLiveData<BaseResponse<List<AsanaCategoryModel>>>()
    val response: LiveData<BaseResponse<List<AsanaCategoryModel>>> = _response

    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int> = _page


    fun getAsanaList() {
        _page.value = page.value?.plus(1)
        launch {
            asanaCatagoryRepository.getAsanaCategory(page.value?:CommonInt.One)
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
        _token.value =
            SharedPreferencesUtils.getUserDetails(sharedPreferences)?.token ?: CommonString.Empty
        _page.value = 0
    }


}