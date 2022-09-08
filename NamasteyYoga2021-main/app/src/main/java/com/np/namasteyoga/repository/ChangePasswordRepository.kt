package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.ChangePasswordModel
import io.reactivex.Single

interface ChangePasswordRepository {
    fun changePassword(changePasswordModel: ChangePasswordModel?,token:String): Single<BaseResponse<EmptyResponse>>
}