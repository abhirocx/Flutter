package com.np.namasteyoga.utils

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import android.content.pm.PackageManager



object RootUtil {
    fun isDeviceRooted(context: Context): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || checkRoot() || checkRootMethod4(context)
    }
    private fun checkRootMethod1(): Boolean {
        val buildTags = android.os.Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))

            val `in` = BufferedReader(InputStreamReader(process!!.inputStream))
            return `in`.readLine() != null
        } catch (t: Throwable) {
            return false
        } finally {
            process?.destroy()
        }
    }

    private fun checkRoot(): Boolean {
        for (pathDir in System.getenv("PATH")!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            if (File(pathDir, "su").exists()) {
                return true
            }
        }
        return false
    }

    private fun checkRootMethod4(context: Context): Boolean {
        return isPackageInstalled("eu.chainfire.supersu", context)
    }

    private fun isPackageInstalled(packagename: String, context: Context): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

    }
}