package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.pojo.RegisterRequest
import com.np.namasteyoga.datasource.pojo.RegisterResponse
import io.reactivex.Single

interface RegisterRepository {

    fun register(registerRequest: RegisterRequest?): Single<RegisterResponse>
}