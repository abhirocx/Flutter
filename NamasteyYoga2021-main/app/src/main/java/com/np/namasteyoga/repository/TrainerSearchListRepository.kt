package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.pojo.UserListRequest
import com.np.namasteyoga.datasource.response.UserListResponse
import io.reactivex.Single


interface TrainerSearchListRepository {
      fun getUserList(userListRequest: UserListRequest,token:String): Single<UserListResponse>
}