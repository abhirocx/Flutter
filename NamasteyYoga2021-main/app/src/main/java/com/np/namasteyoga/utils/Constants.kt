package com.np.namasteyoga.utils

import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.BuildConfig
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


object Constants {
    private const val MD = "MD5"
    private const val CheckSum = "checksum"
    @JvmField var currentYogaType = ""

    fun getMD5EncryptedString(encTarget: String): Map<String, String>? {
        return try {
            val mdEnc = MessageDigest.getInstance(MD)
            val run = mdEnc.run {
                update(encTarget.toByteArray(), CommonInt.Zero, encTarget.length)
                var md5 = BigInteger(CommonInt.One, mdEnc.digest()).toString(16)
                while (md5.length < 32) {
                    md5 = "${CommonInt.Zero}$md5"
                }
                val headers = HashMap<String, String>()
                headers[CheckSum] = md5
                headers
            }
            run
        } catch (e: NoSuchAlgorithmException) {
            if (C.DEBUG)
            e.printStackTrace()
            null
        }

    }
    fun getMD5EncryptedStringWithToken(encTarget: String,token:String): Map<String, String>? {
        return try {
            val mdEnc = MessageDigest.getInstance(MD)
            val run = mdEnc.run {
                update(encTarget.toByteArray(), CommonInt.Zero, encTarget.length)
                var md5 = BigInteger(CommonInt.One, mdEnc.digest()).toString(16)
                while (md5.length < 32) {
                    md5 = "${CommonInt.Zero}$md5"
                }
                val headers = HashMap<String, String>()
                headers[CheckSum] = md5
                headers[C.TOKEN_KEY] = "${C.TOKEN_VALUE} $token"
                headers
            }
            run
        } catch (e: NoSuchAlgorithmException) {
            if (C.DEBUG)
            e.printStackTrace()
            null
        }

    }
}