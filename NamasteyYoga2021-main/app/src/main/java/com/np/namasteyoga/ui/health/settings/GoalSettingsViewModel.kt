package com.np.namasteyoga.ui.health.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.utils.SharedPreferencesUtils

class GoalSettingsViewModel(
    val sharedPreferences: SharedPreferences
) : AbstractViewModel() {

    val response = MutableLiveData<BaseResponse<VersionData>>()
    val goalSettingValue = MutableLiveData<Int>()


    init {
        goalSettingValue.value = SharedPreferencesUtils.getGoalSettingValue(sharedPreferences)
    }
}