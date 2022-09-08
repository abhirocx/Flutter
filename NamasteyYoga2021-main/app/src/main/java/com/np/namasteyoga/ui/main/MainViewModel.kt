package com.np.namasteyoga.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.pojo.RegisterResponse
import com.np.namasteyoga.datasource.response.*
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.MainRepository
import com.np.namasteyoga.repository.SplashRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.np.namasteyoga.utils.UIUtils

class MainViewModel(
    private val mainRepository: MainRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {



    private val _userDetails = MutableLiveData<UserDetail>()
    val userDetails : LiveData<UserDetail>
        get() = _userDetails


    fun getUserDetails(){
        _userDetails.value = SharedPreferencesUtils.getUserDetails(sharedPreferences)
    }
    init {
        getUserDetails()
    }


    private val _responseAsana = MutableLiveData<BaseResponse<List<AsanaCategoryModel>>>()
    val responseAsana: LiveData<BaseResponse<List<AsanaCategoryModel>>> = _responseAsana
    fun getAsanaList() {
        launch {
            mainRepository.getAsanaCategory(CommonInt.One)
                .with(scheduler)
                .subscribe({
                    _responseAsana.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _responseAsana.value = null
                })
        }
    }


    private val _responseAyush = MutableLiveData<BaseResponseNamasteYoga<List<AyushMerchandiseModel>>>()
    val responseAyush: LiveData<BaseResponseNamasteYoga<List<AyushMerchandiseModel>>> = _responseAyush


    fun getAyushCategoryList() {
        launch {
            mainRepository.getAyushCategoryList(CommonInt.One)
                .with(scheduler)
                .subscribe({
                    _responseAyush.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _responseAyush.value = null
                })
        }
    }

    private val _responseCelebrityTestimonial = MutableLiveData<BaseResponseNamasteYoga<List<CelebrityTestimonialModel>>>()
    val responseCelebrityTestimonial: LiveData<BaseResponseNamasteYoga<List<CelebrityTestimonialModel>>> = _responseCelebrityTestimonial


    fun getCelebrityTestimonialList() {
        launch {
            mainRepository.getCelebrityTestimonialList(CommonInt.One)
                .with(scheduler)
                .subscribe({
                    _responseCelebrityTestimonial.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _responseCelebrityTestimonial.value = null
                })
        }
    }


}