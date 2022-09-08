package com.np.namasteyoga.ui.resetPassword

import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.LoginRequest
import com.np.namasteyoga.interfaces.SchedulerProvider
import com.np.namasteyoga.repository.ResetPasswordRepository
import com.np.namasteyoga.utils.Logger

class ResetPasswordViewModel(
    private val resetPasswordRepository: ResetPasswordRepository,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    val response = MutableLiveData<BaseResponse<EmptyResponse>>()
    val responseReset = MutableLiveData<BaseResponse<EmptyResponse>>()
    fun forgotPassword(email: String? = null) {

        launch {
            resetPasswordRepository.forgotPassword(email)
                .with(scheduler)
                .subscribe({
                    response.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    response.value = null
                })
        }
    }
    fun verifyPassword(email: String? = null,newPassword: String? = null,otp: String? = null) {

        launch {

            resetPasswordRepository.verifyPassword(LoginRequest(email,newpass = newPassword,otp = otp))
                .with(scheduler)
                .subscribe({
                    responseReset.value = it
                }, {
                    it?.message?.run(Logger::Error)
                    responseReset.value = null
                })
        }
    }


}