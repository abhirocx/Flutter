package com.np.namasteyoga.ui.login

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.core.content.ContextCompat
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.common.APIStatus
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.button_colored_layout.*
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity<LoginViewModel>(LoginViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UIUtils.setStatusBarGradient(this, R.color.status_bar_color)


        userId.manageEditText(
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
            CommonInt.EmailIdLength,
            getString(R.string.email_id)
        )
        password.manageEditText(
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,
            CommonInt.PasswordLength, getString(R.string.password)
        )
        password.getMyEditText().transformationMethod = PasswordTransformationMethod()
        buttonColorLayoutTxt.setText(R.string.login)

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

            val pwd = password.getMyEditText()
            if (PatternUtil.isNullOrEmpty(pwd.text.toString())) {
                TextUtils.errorEmptyText(pwd, getString(R.string.password))
                return@setOnClickListener
            }
            if (PatternUtil.isValidPassword(pwd.text.toString())) {
                TextUtils.errorValidText(pwd, getString(R.string.password))
                return@setOnClickListener
            }

            login(email.text.toString().trim(), pwd.text.toString().trim())

        }

        password.getMyImageView().setOnClickListener {
            password.updatePasswordVisibility()
        }
        tvLogin.setOnClickListener {
            startRegisterActivity()

        }
        forgotPassword.setOnClickListener {
            startForgotPasswordActivity()
        }


        model.response.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {


                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    SharedPreferencesUtils.setUserDetails(model.sharedPreferences, it.data)
                    setResult(RESULT_OK)
                    finish()
                } else if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_OTHER) {
                    suspendAccountPopup()
                } else {
                    toast(it.message ?: getString(R.string.something_went_wrong))
                }
                Logger.Debug(it.data?.phone.toString())
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })
        model.responseResume.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    toast(R.string.resume_account_message)
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

        model.status.observe(this, {

            when (it?.status) {
                APIStatus.SUCCESS -> {
                    isShowDialog(false)
                    it.msg?.run(::toast)
                }
                APIStatus.FAILURE -> {
                    isShowDialog(false)
                    it.msg?.run(::toast)
                }
                APIStatus.PROCESSING -> {
                    isShowDialog(true)
                }
                APIStatus.WARNING -> {
                    isShowDialog(false)
                    it.msg?.run(::toast)
                }
            }


        })

    }

    override fun onResume() {
        super.onResume()
        userId.clearText()
        password.clearText()
    }

    private fun View.clearText(){
        getMyEditText().run {
            setText(CommonString.Empty)
            error = null
        }
    }
    private fun suspendAccountPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.suspend_account_msg)
            .setPositiveButton(R.string.yes, dialogClickListener)
            .setNegativeButton(R.string.no, dialogClickListener)
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
    }

    private val dialogClickListener =
        DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    resumeAccount()
                }
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
            }
        }

    private fun resumeAccount(){
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.resumeAccount(userId.getMyEditText().text.toString().trim())
        } else
            toast(R.string.no_internet_connection)
    }
    private fun login(
        email: String , pass: String
    ) {
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.login(email, pass)
        } else
            toast(R.string.no_internet_connection)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CommonInt.hundred -> {

                    setResult(RESULT_OK)
                    finishAfterTransition()
                }
            }
        }
    }

    override fun layout(): Int = R.layout.activity_login

    override fun tag(): String = TAG

    companion object {
        private const val TAG = "LoginActivity"
    }

    override val title: String
        get() = TAG

    override val isShowTitle: Boolean
        get() = false
}