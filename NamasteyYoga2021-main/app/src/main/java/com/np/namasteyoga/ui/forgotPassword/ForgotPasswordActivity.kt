package com.np.namasteyoga.ui.forgotPassword

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.ui.resetPassword.ResetPasswordActivity
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_forgot_password.bigBack
import kotlinx.android.synthetic.main.activity_forgot_password.userId
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.button_colored_layout.*
import org.jetbrains.anko.toast

class ForgotPasswordActivity :
    BaseActivity<ForgotPasswordViewModel>(ForgotPasswordViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buttonColorLayoutTxt.setText(R.string.submit)
        bigBack.setOnClickListener {
            finish()
        }
        userId.manageEditText(
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
            CommonInt.EmailIdLength,
            getString(R.string.email_id)
        )
        buttonColorLayout.setOnClickListener {
            val email = userId.getMyEditText()
            if (PatternUtil.isNullOrEmpty(email.text.toString())) {
                TextUtils.errorEmptyText(email, getString(R.string.email_id))
                return@setOnClickListener
            }
            if (PatternUtil.isValidEmail(email.text.toString())) {
                TextUtils.errorValidText(email, getString(R.string.email_id))
                return@setOnClickListener
            }
            forgotPasswordRequest(email.text.toString().trim().toLowerCase())
        }

        model.response.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {

                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    startResetPasswordActivity()
                }else{
                    toast(it.message ?: getString(R.string.something_went_wrong))
                }

            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })
    }

    private fun startResetPasswordActivity() {
        val intent = Intent(context,ResetPasswordActivity::class.java)
        intent.putExtra(IntentUtils.SHARE_P_ID,userId.getMyEditText().text.toString().trim())
        startActivityForResult(intent,CommonInt.hundredOne)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonInt.hundredOne && resultCode == RESULT_OK){
            finishAfterTransition()
        }
    }
    private fun forgotPasswordRequest(email: String) {
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.forgotPassword(email)
        } else
            toast(R.string.no_internet_connection)
    }

    override fun layout(): Int = R.layout.activity_forgot_password

    override fun tag(): String = TAG

    companion object {
        private const val TAG = "ForgotPasswordActivity:: "
    }

    override val title: String
        get() = CommonString.Empty
    override val isShowTitle: Boolean
        get() = false
}