package com.np.namasteyoga.ui.resetPassword

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.bigBack
import kotlinx.android.synthetic.main.button_colored_layout.*
import org.jetbrains.anko.toast

class ResetPasswordActivity : BaseActivity<ResetPasswordViewModel>(ResetPasswordViewModel::class) {


    private var countDown = 60
    private val countDownTimer = object : CountDownTimer(60000, 1000) {
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            resendOTP.disable()
            resendOTP.text = "00:$countDown"
            countDown--
        }

        override fun onFinish() {
            resendOTP.setText(R.string.resendOTP)
            resendOTP.enable()
            countDown = 60

        }
    }

    private var emailId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        intent?.run {
            if (intent.hasExtra(IntentUtils.SHARE_P_ID)) {
                emailId = intent.getStringExtra(IntentUtils.SHARE_P_ID)
            }
        }
        bigBack.setOnClickListener {
            finish()
        }
        buttonColorLayoutTxt.setText(R.string.submit)
        etPassword.getMyEditText().transformationMethod = PasswordTransformationMethod()
        etConfirmPassword.getMyEditText().transformationMethod = PasswordTransformationMethod()


        etPassword.manageEditText(
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,
            CommonInt.PasswordLength, getString(R.string.new_password)
        )
        etConfirmPassword.manageEditText(
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,
            CommonInt.PasswordLength, getString(R.string.confirm_password)
        )


        etPassword.getMyImageView().setOnClickListener { etPassword.updatePasswordVisibility() }
        etConfirmPassword.getMyImageView()
            .setOnClickListener { etConfirmPassword.updatePasswordVisibility() }
        resendOTP.setOnClickListener {
            forgotPasswordRequest()
        }
        buttonColorLayout.setOnClickListener {

            val otp = etOTP.getMyEditText()
            if (PatternUtil.isNullOrEmpty(otp.text.toString())) {
                TextUtils.errorEmptyText(otp, getString(R.string.enterOTP))
                return@setOnClickListener
            }
            if (PatternUtil.isValidOTP(otp.text.toString())) {
                TextUtils.errorValidText(otp, getString(R.string.enterOTP))
                return@setOnClickListener
            }

            val newPasswordStr = etPassword.getMyEditText()
            if (PatternUtil.isNullOrEmpty(newPasswordStr.text.toString())) {
                TextUtils.errorEmptyText(newPasswordStr, getString(R.string.new_password))
                return@setOnClickListener
            }
            if (PatternUtil.isValidPassword(newPasswordStr.text.toString())) {
                TextUtils.passwordValidationMsg(newPasswordStr)
                return@setOnClickListener
            }


            val confirmPasswordStr = etConfirmPassword.getMyEditText()
            if (PatternUtil.isNullOrEmpty(confirmPasswordStr.text.toString())) {
                TextUtils.errorEmptyText(confirmPasswordStr, getString(R.string.confirm_password))
                return@setOnClickListener
            }
            val passNew = newPasswordStr.text.toString().trim()
            val passConfirm = confirmPasswordStr.text.toString().trim()
            if (passNew != passConfirm) {
                TextUtils.errorNotMatchText(
                    confirmPasswordStr,
                    getString(R.string.new_password),
                    getString(R.string.confirm_password)
                )
                return@setOnClickListener
            }
            verifyPasswordRequest(otp.text.toString().trim(), passNew)
        }

        model.response.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {
                toast(it.message ?: getString(R.string.something_went_wrong))
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    countDownTimer.start()
                }

            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })
        model.responseReset.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {
                toast(it.message ?: getString(R.string.something_went_wrong))
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    setResult(RESULT_OK)
                    finish()
                }

            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })

    }

    private fun forgotPasswordRequest() {
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.forgotPassword(emailId)
        } else
            toast(R.string.no_internet_connection)
    }

    private fun verifyPasswordRequest(otp: String, password: String) {
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.verifyPassword(email = emailId, newPassword = password, otp)
        } else
            toast(R.string.no_internet_connection)
    }

    companion object {
        private const val TAG = "ResetPasswordActivity :::  "
    }

    override fun layout(): Int = R.layout.activity_reset_password

    override fun tag(): String = TAG

    override val title: String
        get() = TAG

    override val isShowTitle: Boolean
        get() = false
}