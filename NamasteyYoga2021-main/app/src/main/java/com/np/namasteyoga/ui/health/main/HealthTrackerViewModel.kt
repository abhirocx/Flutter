package com.np.namasteyoga.ui.health.main

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.utils.SharedPreferencesUtils

class HealthTrackerViewModel(
    val sharedPreferences: SharedPreferences
) : AbstractViewModel() {

    val response = MutableLiveData<BaseResponse<VersionData>>()
    val goalSettingValue = MutableLiveData<Int>()
    val stepCountValue = MutableLiveData<Int>()


    fun updateStepGoalData(value: Int = CommonInt.GoalDefaultValue) {
        SharedPreferencesUtils.setGoalSettingValue(sharedPreferences, value)
        goalSettingValue.value = value
    }

    init {
        goalSettingValue.value = SharedPreferencesUtils.getGoalSettingValue(sharedPreferences)
    }

}