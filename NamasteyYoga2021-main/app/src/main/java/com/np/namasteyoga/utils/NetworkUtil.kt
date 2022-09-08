package com.np.namasteyoga.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.np.namasteyoga.interfaces.ConnectionListener
import com.np.namasteyoga.R


object NetworkUtil {



    @Suppress("DEPRECATION")
    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    fun showNetworkPopup(context: Context,connectionListener: ConnectionListener?=null) {
        try {
            val alertBuilder = AlertDialog.Builder(context)
            alertBuilder.setCancelable(false)
            alertBuilder.setTitle(R.string.no_internet)
            alertBuilder.setMessage(R.string.please_check_internet)
            alertBuilder.setPositiveButton(R.string.ok
            ) { dialog, _ ->
                dialog.dismiss()
                connectionListener?.onClick()
            }
            val alert = alertBuilder.create()
            alert.show()
        } catch (e: Exception) {
        }
    }





}