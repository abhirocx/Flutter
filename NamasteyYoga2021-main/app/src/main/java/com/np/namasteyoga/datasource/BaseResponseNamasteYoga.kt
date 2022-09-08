package com.np.namasteyoga.datasource

import com.np.namasteyoga.utils.C

data class BaseResponseNamasteYoga<T> (
        val status:String?= C.NP_STATUS_FAIL,
        val message:String?=null,
        val data:T?=null,
        val total_record:Int=1,
        val last_page:Int=1,
        val current_page:Int=1
):Response()