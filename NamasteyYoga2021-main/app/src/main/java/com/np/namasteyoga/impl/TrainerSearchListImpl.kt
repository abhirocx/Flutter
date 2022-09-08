package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.datasource.pojo.UserListRequest
import com.np.namasteyoga.datasource.response.UserListResponse
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.TrainerSearchListRepository
import com.np.namasteyoga.utils.Constants
import io.reactivex.Single

class TrainerSearchListImpl(private val restApi: RESTApi) : TrainerSearchListRepository {
    override fun getUserList(
        userListRequest: UserListRequest,
        token: String
    ): Single<UserListResponse> {
        return try {
            val json = Gson().toJson(userListRequest)
            val header = Constants.getMD5EncryptedStringWithToken(json, token)
            restApi.getSearchUserList(userListRequest, header)
        } catch (e: Exception) {
            Single.error(Throwable(e))
        }
    }

}