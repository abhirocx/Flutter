package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.pojo.EditRequest
import com.np.namasteyoga.datasource.pojo.RegisterResponse
import com.np.namasteyoga.datasource.response.UserDetail
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.AccountUpdateRepository
import com.np.namasteyoga.repository.MainRepository
import com.np.namasteyoga.utils.ConstUtility
import com.np.namasteyoga.utils.Constants
import io.reactivex.Single

class AccountUpdateImpl (private val restApi: RESTApi):AccountUpdateRepository{
    override fun updateAccount(editRequest: EditRequest?,token:String): Single<BaseResponse<UserDetail>> {
        if (editRequest?.phone == null ) {
            return Single.error(Throwable(CommonString.DATA_NULL))
        }
        return try {
            val _phone = ConstUtility.encryptLC(editRequest.phone)

            editRequest.run {
                phone = _phone
            }
            val json = Gson().toJson(editRequest)
            val header = Constants.getMD5EncryptedStringWithToken(json,token)
            restApi.updateAccount(editRequest, header)
        } catch (e: Exception) {
            Single.error(Throwable(e))
        }
    }

}