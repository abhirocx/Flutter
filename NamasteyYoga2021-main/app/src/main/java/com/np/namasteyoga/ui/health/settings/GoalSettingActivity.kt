package com.np.namasteyoga.ui.health.settings

import android.content.Intent
import android.os.Bundle
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.utils.IntentUtils
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.setColoredButtonText
import kotlinx.android.synthetic.main.activity_goal_setting.*
import java.util.*

class GoalSettingActivity : BaseActivity<GoalSettingsViewModel>(GoalSettingsViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cancel_action.setColoredButtonText(getString(R.string.cancel))
        saveAction.setColoredButtonText(getString(R.string.save))
        cancel_action.setOnClickListener {
            finish()
        }
        target.text = getString(R.string.today_target_s,model.goalSettingValue.value.toString())
        val index = resources.getStringArray(R.array.WheelArrayWeek).indexOf(model.goalSettingValue.value.toString())
        main_wheel_left.setSelectedItemPosition(index)
        saveAction.setOnClickListener {
            Logger.Debug("Data:: ${main_wheel_left.currentItemPosition}  ${main_wheel_left.selectedItemPosition}")
            val data = resources.getStringArray(R.array.WheelArrayWeek)[main_wheel_left.currentItemPosition]
            val intent = Intent()
            intent.putExtra(IntentUtils.SHARE_GOAL_SETTINGS_VALUE,data.toInt())
            setResult(RESULT_OK,intent)
            finish()
        }
        main_wheel_left.data= resources.getStringArray(R.array.WheelArrayWeek).asList()
        main_wheel_left.setSelectedItem(index)


    }

    companion object{
        private const val TAG = "GoalSettingActivity::  "
    }
    override fun layout(): Int = R.layout.activity_goal_setting

    override fun tag(): String = TAG

    override val title: String
        get() = getString(R.string.goal_setting)
    override val isShowTitle: Boolean
        get() = true
}