package com.np.namasteyoga.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.np.namasteyoga.comman.CommonBoolean
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.datasource.response.UserDetail


object SharedPreferencesUtils {
    private const val isLocationPopup = "is_location_popup"
    private const val isImagePopup = "is_image_popup"
    private const val isCameraPopup = "is_camera_popup"
    private const val isEmployeeLogin = "is_employee_login"
    private const val user_detail = "user_detail"
    private const val pushTokenOnOff = "push_token_settings"
    private const val pushToken = "push_token"
    private const val pushTokenNew = "push_token_new"
    private const val isNormalUser = "is_user"
    private const val isFirstTimeLogin = "is_first_time_login"
    private const val isFirstTimeOnPaper = "is_first_time_on_paper"
    private const val isInventoryKey = "is_inventory_key"

    private const val goalSettingValueKey = "goal_settings"

    private const val languageSetString = "language_set_string"
    const val defaultLanguage = "en"
    const val SecondLanguage = "hi"


    fun getPushToken(sharedPreferences: SharedPreferences): String? {
        return sharedPreferences.getString(pushToken, null)
    }

    fun setPushToken(sharedPreferences: SharedPreferences, data: String?) {
        sharedPreferences.edit().putString(pushToken, data).apply()
    }


    fun getPushTokenOnOff(sharedPreferences: SharedPreferences): Int{
        return sharedPreferences.getInt(pushTokenOnOff, 1)
    }

    fun setPushTokenOnOff(sharedPreferences: SharedPreferences, data: Int) {
        sharedPreferences.edit().putInt(pushTokenOnOff, data).apply()
    }

    fun getPushTokenNew(sharedPreferences: SharedPreferences): String? {
        return sharedPreferences.getString(pushTokenNew, null)
    }

    fun setPushTokenNew(sharedPreferences: SharedPreferences, data: String?) {
        sharedPreferences.edit().putString(pushTokenNew, data).apply()
    }

    fun getIsShowLocationPopup(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(isLocationPopup, CommonBoolean.FALSE)
    }

    fun setIsShowLocationPopup(sharedPreferences: SharedPreferences, data: Boolean) {
        sharedPreferences.edit().putBoolean(isLocationPopup, data).apply()
    }

    fun getIsShowGalleryPopup(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(isImagePopup, CommonBoolean.FALSE)
    }

    fun setIsShowGalleryPopup(sharedPreferences: SharedPreferences, data: Boolean) {
        sharedPreferences.edit().putBoolean(isImagePopup, data).apply()
    }

    fun getIsShowCameraPopup(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(isCameraPopup, CommonBoolean.FALSE)
    }

    fun setIsShowCameraPopup(sharedPreferences: SharedPreferences, data: Boolean) {
        sharedPreferences.edit().putBoolean(isCameraPopup, data).apply()
    }


    fun getUserDetails(sharedPreferences: SharedPreferences): UserDetail? {
        val dataString = sharedPreferences.getString(user_detail, null)
        dataString?.run {
            return Gson().fromJson(this, UserDetail::class.java)
        }
        return null
    }

    fun setUserDetails(sharedPreferences: SharedPreferences, data: UserDetail?) {
        val json = GsonBuilder().create().toJson(data)
        sharedPreferences.edit().putString(user_detail, json).apply()
    }

    fun getUserType(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(isNormalUser, CommonBoolean.FALSE)
    }

    fun setUserType(sharedPreferences: SharedPreferences, data: Boolean) {
        sharedPreferences.edit().putBoolean(isNormalUser, data).apply()
    }

    fun getIsInventoryKey(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(isInventoryKey, CommonBoolean.FALSE)
    }

    fun setIsInventoryKey(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().putBoolean(isInventoryKey, true).apply()
    }


    fun getIsEmployeeLogin(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(isEmployeeLogin, CommonBoolean.FALSE)
    }

    fun setIsEmployeeLogin(sharedPreferences: SharedPreferences, isEmp: Boolean) {
        sharedPreferences.edit().putBoolean(isEmployeeLogin, isEmp).apply()
    }


    fun getIsFirstTimeLogin(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(isFirstTimeLogin, CommonBoolean.FALSE)
    }

    fun setIsFirstTimeLogin(sharedPreferences: SharedPreferences, isEmp: Boolean) {
        sharedPreferences.edit().putBoolean(isFirstTimeLogin, isEmp).apply()
    }

    fun getIsFirstTimeOnPaper(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(isFirstTimeOnPaper, CommonBoolean.FALSE)
    }

    fun setIsFirstTimeOnPaper(sharedPreferences: SharedPreferences, isEmp: Boolean) {
        sharedPreferences.edit().putBoolean(isFirstTimeOnPaper, isEmp).apply()
    }


    fun getGoalSettingValue(sharedPreferences: SharedPreferences): Int {
        return sharedPreferences.getInt(goalSettingValueKey, CommonInt.GoalDefaultValue)
    }

    fun setGoalSettingValue(
        sharedPreferences: SharedPreferences,
        isEmp: Int = CommonInt.GoalDefaultValue
    ) {
        sharedPreferences.edit().putInt(goalSettingValueKey, isEmp).apply()
    }

    fun setLanguageString(sharedPreferences: SharedPreferences ,lan:String= defaultLanguage){
        sharedPreferences.edit().putString(languageSetString,lan).apply()
    }

    fun getLanguageString(sharedPreferences: SharedPreferences):String{
        return sharedPreferences.getString(languageSetString, defaultLanguage)?: defaultLanguage
    }

}