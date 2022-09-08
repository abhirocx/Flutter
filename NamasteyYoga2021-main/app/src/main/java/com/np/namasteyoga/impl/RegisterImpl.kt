package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.RegisterRequest
import com.np.namasteyoga.datasource.pojo.RegisterResponse
import com.np.namasteyoga.datasource.request.LoginRequest
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.RegisterRepository
import com.np.namasteyoga.utils.ConstUtility
import com.np.namasteyoga.utils.Constants
import io.reactivex.Single

class RegisterImpl(private val restApi: RESTApi) : RegisterRepository {
    override fun register(registerRequest: RegisterRequest?): Single<RegisterResponse> {
        if (registerRequest?.password == null || registerRequest.phone == null || registerRequest.email == null) {
            return Single.error(Throwable(CommonString.DATA_NULL))
        }
        return try {
            val _email = ConstUtility.encryptLC(registerRequest.email)
            val _newpass = ConstUtility.encryptLC(registerRequest.password)
            val _phone = ConstUtility.encryptLC(registerRequest.phone)

            val register = clone(data = registerRequest)
            register.run {
                email = _email
                password = _newpass
                passwordConfirmation = _newpass
                phone = _phone
            }


            val json = Gson().toJson(register)
            val header = Constants.getMD5EncryptedString(json)
            restApi.register(register, header)
        } catch (e: Exception) {
            Single.error(Throwable(e))
        }
    }

    private fun clone(data: RegisterRequest): RegisterRequest {
        val stringAnimal = Gson().toJson(data, RegisterRequest::class.java)
        return Gson().fromJson(stringAnimal, RegisterRequest::class.java)
    }
}