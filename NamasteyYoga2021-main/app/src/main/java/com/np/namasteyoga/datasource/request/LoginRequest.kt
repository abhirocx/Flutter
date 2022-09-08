package com.np.namasteyoga.datasource.request


data class LoginRequest(
    var email: String? = null,
    var password: String? = null,
    var newpass: String? = null,
    var otp: String? = null
)