package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.datasource.pojo.UserListRequest
import com.np.namasteyoga.datasource.request.BhuvanListRequest
import com.np.namasteyoga.datasource.response.BhuvanAppListResponseModel
import com.np.namasteyoga.datasource.response.UserListResponse
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.TrainerListRepository
import com.np.namasteyoga.utils.Constants
import io.reactivex.Single

class TrainerListImpl(private val restApi: RESTApi):TrainerListRepository {
    override fun getUserList(userListRequest: UserListRequest,token:String): Single<UserListResponse> {
        return try {
            val json = Gson().toJson(userListRequest)
            val header = Constants.getMD5EncryptedStringWithToken(json,token)
            restApi.getUserList(userListRequest, header)
        }catch (e:Exception){
            Single.error(Throwable(e))
        }
    }

        override fun getBhuvanList(
        bhuvanListRequest: BhuvanListRequest,
        token: String
    ): Single<BhuvanAppListResponseModel> {
        return try {
            val json = Gson().toJson(bhuvanListRequest)
            val header = Constants.getMD5EncryptedStringWithToken(json,token)
            restApi.getBhuvanList(bhuvanListRequest, header)
        }catch (e:Exception){
            Single.error(Throwable(e))
        }
    }

}