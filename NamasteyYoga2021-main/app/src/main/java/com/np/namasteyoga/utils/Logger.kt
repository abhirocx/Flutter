package com.np.namasteyoga.utils

import android.util.Log
import com.np.namasteyoga.BuildConfig


object Logger {

    const val TAG = "Logger"

    fun Debug(msg:String,tag:String ?= TAG){
        if (C.DEBUG){
            Log.d(tag,msg)
        }

    }
    fun Debug(msg:Exception?,tag:String= TAG){
        if (C.DEBUG){
            Log.d(tag,msg?.localizedMessage!!)
        }
    }

    fun Error( msg:String,tag:String= TAG){
        if (C.DEBUG){
            Log.e(tag,msg)
        }
    }
    fun Error( msg:Exception?,tag:String= TAG){
        if (C.DEBUG){
            Log.e(tag,msg?.localizedMessage!!)
        }
    }

}