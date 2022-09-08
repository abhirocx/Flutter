package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.pojo.UserListRequest
import com.np.namasteyoga.datasource.request.BhuvanListRequest
import com.np.namasteyoga.datasource.response.BhuvanAppListResponseModel
import com.np.namasteyoga.datasource.response.UserListResponse
import io.reactivex.Single


interface TrainerListRepository {
      fun getUserList(userListRequest: UserListRequest,token:String): Single<UserListResponse>
      fun getBhuvanList(bhuvanListRequest: BhuvanListRequest,token:String): Single<BhuvanAppListResponseModel>

}