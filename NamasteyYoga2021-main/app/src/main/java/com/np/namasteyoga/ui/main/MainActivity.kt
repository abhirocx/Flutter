package com.np.namasteyoga.ui.main

import android.os.Bundle
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonString
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.trainer

class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        changePassword.setOnClickListener {
            startChangePasswordActivity()
        }
        trainer.setOnClickListener {
            startTrainerListActivity()
        }
        center.setOnClickListener {
            startCenterListActivity()
        }
        pushNotification.setOnClickListener {
            startTestFCMActivity()
        }
        healthTracker.setOnClickListener {
            startHealthActivity()
        }




    }

    override fun layout(): Int = R.layout.activity_main

    override fun tag(): String = TAG

    companion object{
        private const val TAG = "MainActivity::: "
    }

    override val title: String
        get() = CommonString.Empty
    override val isShowTitle: Boolean
        get() = false


}
