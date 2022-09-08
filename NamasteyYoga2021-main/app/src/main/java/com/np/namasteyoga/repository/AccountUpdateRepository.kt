package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.pojo.EditRequest
import com.np.namasteyoga.datasource.response.UserDetail
import io.reactivex.Single


interface AccountUpdateRepository {
    fun updateAccount(editRequest: EditRequest?,token:String): Single<BaseResponse<UserDetail>>
}