package com.np.namasteyoga.datasource.request

data class ChangePasswordModel(
    val confirm_password: String? = null,
    val new_password: String? = null,
    val old_password: String? = null
)