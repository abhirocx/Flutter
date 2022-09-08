package com.np.namasteyoga.ui.onPaperBoard

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.np.namasteyoga.base.AbstractViewModel
import com.np.namasteyoga.interfaces.SchedulerProvider

class OnPaperBoardViewModel(
    val sharedPreferences: SharedPreferences,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    val response = MutableLiveData<Int>()





}