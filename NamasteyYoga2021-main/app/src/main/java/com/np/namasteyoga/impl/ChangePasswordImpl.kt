package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.request.ChangePasswordModel
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.ChangePasswordRepository
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.ConstUtility
import com.np.namasteyoga.utils.Constants
import io.reactivex.Single

class ChangePasswordImpl(private val restApi: RESTApi):ChangePasswordRepository {
    override fun changePassword(changePasswordModel: ChangePasswordModel?, token: String): Single<BaseResponse<EmptyResponse>> {
        if (changePasswordModel?.old_password == null || changePasswordModel.new_password == null) {
            return Single.error(Throwable(CommonString.DATA_NULL))
        }
        return try {
            val _oldPassword = ConstUtility.encryptLC(changePasswordModel.old_password)
            val _newPassword = ConstUtility.encryptLC(changePasswordModel.new_password)
            val _changePasswordModel = ChangePasswordModel(_newPassword, _newPassword, _oldPassword)
            val json = Gson().toJson(_changePasswordModel)
            val header = Constants.getMD5EncryptedStringWithToken(json,token)
            restApi.changePassword(_changePasswordModel, header)
        } catch (e: Exception) {
            Single.error(Throwable(e))
        }
    }

}