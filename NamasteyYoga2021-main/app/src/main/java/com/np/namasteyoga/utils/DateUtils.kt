package com.np.namasteyoga.utils

import android.annotation.SuppressLint
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {

    @SuppressLint("SimpleDateFormat")
    fun getDataString(pattern:String, timeInMill: Long):String{
        val outputFormat = SimpleDateFormat(pattern)
        return outputFormat.format(Date(timeInMill))
    }
    @SuppressLint("SimpleDateFormat")
    fun getDateToString(str:String):Date?{
       return try {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str)
//            SimpleDateFormat("").parse(str)
        }catch (e:Exception){
            null
        }
    }

}