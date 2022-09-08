package com.np.namasteyoga.datasource.request

import com.np.namasteyoga.comman.CommonInt

data class FCMTokenUpdateModel(
    val new_device_id: String? = null,
    val old_device_id: String? = null,
    val user_id: String? = null,
    val notificationSetting: Int = CommonInt.One
)