package com.np.namasteyoga.ui.fcmTestActivity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.utils.NetworkUtil
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.np.namasteyoga.utils.UIUtils
import kotlinx.android.synthetic.main.activity_test_fcm.*
import kotlinx.android.synthetic.main.button_colored_layout.*
import org.jetbrains.anko.toast

class TestFcmActivity : BaseActivity<TestFCMViewModel>(TestFCMViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UIUtils.setToken(model.sharedPreferences)


        buttonColorLayout.setOnClickListener {
            updateFCMToken()
        }
        setToken()
        copy.setOnClickListener {
            copyTextToClipboard()
        }
        buttonColorLayoutTxt.text = getString(R.string.save_token)
        updateFCMToken()
        model.response.observe(this, {
            isShowDialog(false)
            toast(it?.message ?: getString(R.string.something_went_wrong))
        })

    }

    private fun setToken() {
        val token = SharedPreferencesUtils.getPushTokenNew(model.sharedPreferences)
        txtToken.text = token
    }

    private fun updateFCMToken() {
        UIUtils.setToken(model.sharedPreferences)
        setToken()

        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.updateFCMToken()
        } else
            toast(R.string.no_internet_connection)
    }


    private fun copyTextToClipboard() {
        val textToCopy = txtToken.text

        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)

        toast("Text copied to clipboard")
    }

    companion object {
        private const val TAG = "TestFcmActivity::  "
    }

    override fun layout(): Int = R.layout.activity_test_fcm

    override fun tag(): String = TAG

    override val title: String
        get() = TAG

    override val isShowTitle: Boolean
        get() = false
}