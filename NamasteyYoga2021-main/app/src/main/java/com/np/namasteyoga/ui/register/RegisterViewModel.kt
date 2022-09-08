package com.np.namasteyoga.ui.register

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.pojo.RegisterRequest
import com.np.namasteyoga.datasource.pojo.RegisterResponse
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.RegisterRepository
import com.np.namasteyoga.repository.SplashRepository
import com.np.namasteyoga.utils.Logger
import io.reactivex.Single

class RegisterViewModel(
    private val registerRepository: RegisterRepository,
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    private val _responseRegister = MutableLiveData<RegisterResponse>()
    val response : LiveData<RegisterResponse>
        get() = _responseRegister


    fun register(registerRequest: RegisterRequest) {

        launch {
            registerRepository.register(registerRequest)
                .with(scheduler)
                .subscribe({
                    _responseRegister.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    _responseRegister.value = null
                })
        }
    }


}