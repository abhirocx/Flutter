package com.np.namasteyoga.ui.forgotPassword

import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.ForgotPasswordRepository
import com.np.namasteyoga.utils.Logger

class ForgotPasswordViewModel(
    private val forgotPasswordRepository: ForgotPasswordRepository,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    val response = MutableLiveData<BaseResponse<EmptyResponse>>()
    fun forgotPassword(email: String? = null) {

        launch {
            forgotPasswordRepository.forgotPassword(email)
                .with(scheduler)
                .subscribe({
                    response.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    response.value = null
                })
        }
    }


}