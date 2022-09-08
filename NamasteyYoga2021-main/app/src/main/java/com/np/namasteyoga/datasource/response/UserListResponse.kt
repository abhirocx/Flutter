package com.np.namasteyoga.datasource.response

data class UserListResponse(
    val current_page: Int? = null,
    val `data`: List<User>? = null,
    val last_page: Int? = null,
    val message: String? = null,
//    val status: Int? = null,
    val status: String? = null,
    val total_record: Int? = null
)