package com.np.namasteyoga.ui.health.history

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.response.VersionData
import com.np.namasteyoga.utils.SharedPreferencesUtils

class StepHistoryViewModel(
    val sharedPreferences: SharedPreferences
) : AbstractViewModel() {

    val goalSettingValue = MutableLiveData<Int>()

    init {
        goalSettingValue.value = SharedPreferencesUtils.getGoalSettingValue(sharedPreferences)
    }



}