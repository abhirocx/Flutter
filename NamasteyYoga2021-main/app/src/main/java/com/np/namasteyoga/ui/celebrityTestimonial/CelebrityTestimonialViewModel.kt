package com.np.namasteyoga.ui.celebrityTestimonial

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.UserListRequest
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import com.np.namasteyoga.datasource.response.AyushMerchandiseModel
import com.np.namasteyoga.datasource.response.CelebrityTestimonialModel
import com.np.namasteyoga.datasource.response.UserListResponse
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.AsanaCatagoryRepository
import com.np.namasteyoga.repository.AyushCatagoryRepository
import com.np.namasteyoga.repository.CelebrityTestimonialRepository
import com.np.namasteyoga.repository.TrainerListRepository
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils

class CelebrityTestimonialViewModel(
    private val celebrityTestimonialRepository: CelebrityTestimonialRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    private val _token = MutableLiveData<String>()
    private val token: LiveData<String> = _token



    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int> = _page


    private val _responseCelebrityTestimonial = MutableLiveData<BaseResponseNamasteYoga<List<CelebrityTestimonialModel>>>()
    val responseCelebrityTestimonial: LiveData<BaseResponseNamasteYoga<List<CelebrityTestimonialModel>>> = _responseCelebrityTestimonial


    fun getCelebrityTestimonialList() {
        _page.value = page.value?.plus(1)
        launch {
            celebrityTestimonialRepository.getCelebrityTestimonialList(page.value?:CommonInt.One)
                .with(scheduler)
                .subscribe({
                    _responseCelebrityTestimonial.value = it
                },{
                    it?.message?.run(Logger::Error)
                    _responseCelebrityTestimonial.value = null
                })
        }
    }



    init {
        _token.value =
            SharedPreferencesUtils.getUserDetails(sharedPreferences)?.token ?: CommonString.Empty
        _page.value = 0
    }


}