package com.np.namasteyoga.ui.joiningInstructions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.Event
import com.np.namasteyoga.ui.login.LoginActivity
import com.np.namasteyoga.utils.*
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import kotlinx.android.synthetic.main.activity_joining.*
import kotlinx.android.synthetic.main.toolbar_black_with_back.*
import org.jetbrains.anko.toast

class JoiningInstructionsActivity :
    BaseActivity<JoiningInstructionsViewModel>(JoiningInstructionsViewModel::class) {


    private var event: Event? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.run {
            if (hasExtra(IntentUtils.SHARE_RESULT)) {
                event = (getSerializableExtra(IntentUtils.SHARE_RESULT) as Event?)
                event?.run {
                    tvJoiningInstruction.text = joining_instruction
                    toolbarTitle.text = this.eventName?.getCapitalized()
                    if (meeting_link.isNullOrEmpty()) {
                        meetingLayout.hide()
                    } else {
                        meetingLink.text = meeting_link
                    }

                }
            }
        }

        tvIntrested.setOnClickListener {
            if (model.userDetails.value?.token == null) {
                startActivityForResult(Intent(context, LoginActivity::class.java), 100)
                return@setOnClickListener
            }
            interested()
        }
        meetingLink.setOnClickListener {
            var url = meetingLink.text.toString()
            if (!url.startsWith(CommonString.HTTP)){
                url = "${CommonString.HTTP}://$url"
            }
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
        model.response.observe(this, Observer {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@Observer
            }

            try {
                val status = (it.status ?: C.NP_STATUS_FAIL)
                if (status == C.NP_INVALID_TOKEN || status == C.NP_TOKEN_EXPIRED || status == C.NP_TOKEN_NOT_FOUND) {
                    SharedPreferencesUtils.setUserDetails(model.sharedPreferences, null)
                    setResult(RESULT_OK)
                    finish()
                    return@Observer
                }

                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    toast(it.message ?: getString(R.string.something_went_wrong))
                    setResult(RESULT_OK)
                    finish()
                } else {
                    toast(it.message ?: getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })
    }

    private fun interested() {
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.interested(event?.id?.toString() ?: CommonString.Empty)
        } else {
            toast(R.string.no_internet_connection)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        model.getUserDetails()
    }

    override fun layout(): Int = R.layout.activity_joining

    companion object {
        private const val TAG = "JoiningInstructionsActivity ::"
    }

    override fun tag(): String = TAG

    override val title: String
        get() = getString(R.string.joining_instructions)
    override val isShowTitle: Boolean
        get() = true
}