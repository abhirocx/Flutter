package com.np.namasteyoga.datasource


data class BaseResponse<T> (
//        val status:Int?=CommonInt.Zero,
        val status:String?=null,
//        val StatusCode:String?=null,
        val message:String?=null,
        val data:T?=null
):Response()