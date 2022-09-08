package com.np.namasteyoga.utils

import java.util.regex.Pattern

object PatternUtil {

    private const val ContactPattern = "^[1-9]{1}[0-9]{9}$"
    private const val NamePattern = "^[a-zA-Z]{3,50}$"
//    private const val FullNamePattern = "^[a-zA-Z ]{3,100}$"
//    private const val FullNamePattern = "^[a-zA-Z0-9 ]{3,100}$"
    private const val FullNamePattern = "^[[A-Za-z]{1}a-zA-Z' ]{3,99}$"
    private const val DesignationPattern = "^[a-zA-Z]{1,50}[a-zA-Z0-9-]{2,50}$"
    private const val UserNamePattern = "^[a-zA-Z]{1,50}[a-zA-Z0-9-@_.]{2,50}$"
    private const val AddressPattern = "^[a-zA-Z0-9,/ -]{2,255}$"
    private const val OTPPattern = "^[0-9]{6}$"
    private const val EmailPattern = "^[a-zA-Z0-9._-][a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"
    private const val PasswordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})"
    private const val IFSCPattern = "^[A-Za-z]{4}0[A-Z0-9a-z]{6}$"
    private const val AccountNoPattern = "^[A-Za-z0-9]{9,36}$"
    private const val YouTubeURLPattern = "^.*(youtube|youtu.be).*$"
    private const val CertificateNo_Pattern = "^(?=.*[0-9])(?=.*[a-z,A-Z])(?=\\S+$).{3,16}$"


    fun isNullOrEmpty(str: String?): Boolean {
        return (str == null || str.trim() == "")
    }


    fun isValidMobile(str: String): Boolean {
        return !str.trim().matches(ContactPattern.toRegex())
    }
    fun isValidEmail(str: String): Boolean {
        return !str.trim().matches(EmailPattern.toRegex())
    }

    fun isGreaterThan(str: String, len: Int): Boolean {
        return !(str.trim().length>len)
    }

    fun isValidPassword(str: String): Boolean {
        return !str.trim().matches(PasswordPattern.toRegex())
    }

    fun isValidOTP(str: String): Boolean {
        return !str.trim().matches(OTPPattern.toRegex())
    }
    fun isValidName(str: String): Boolean {
        return !str.trim().matches(FullNamePattern.toRegex())
    }
    fun isValidCertificate(str: String): Boolean {
        return !str.trim().matches(CertificateNo_Pattern.toRegex())
    }
    fun isValidAddress(str: String): Boolean {
        return !str.trim().matches(AddressPattern.toRegex())
    }

}