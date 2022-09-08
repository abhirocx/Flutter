package com.np.namasteyoga.ui.changePassword

import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.button_colored_layout.*
import org.jetbrains.anko.toast

class ChangePasswordActivity : BaseActivity<ChangePasswordViewModel>(ChangePasswordViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UIUtils.setStatusBarGradient(this,R.color.status_bar_color)

        bigBack.setOnClickListener {
            finish()
        }
        buttonColorLayoutTxt.setText(R.string.submit)
        oldPassword.getMyEditText().transformationMethod = PasswordTransformationMethod()
        newPassword.getMyEditText().transformationMethod = PasswordTransformationMethod()
        confirmPassword.getMyEditText().transformationMethod = PasswordTransformationMethod()

        oldPassword.manageEditText(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,CommonInt.PasswordLength,getString(R.string.old_password))
        newPassword.manageEditText(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,CommonInt.PasswordLength,getString(R.string.new_password))
        confirmPassword.manageEditText(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,CommonInt.PasswordLength,getString(R.string.confirm_password))

        oldPassword.getMyImageView().setOnClickListener { oldPassword.updatePasswordVisibility() }
        newPassword.getMyImageView().setOnClickListener { newPassword.updatePasswordVisibility() }
        confirmPassword.getMyImageView().setOnClickListener { confirmPassword.updatePasswordVisibility() }
        buttonColorLayout.setOnClickListener {
            val oldPasswordStr = oldPassword.getMyEditText()
            if (PatternUtil.isNullOrEmpty(oldPasswordStr.text.toString())) {
                TextUtils.errorEmptyText(oldPasswordStr, getString(R.string.old_password))
                return@setOnClickListener
            }
            if (PatternUtil.isValidPassword(oldPasswordStr.text.toString())) {
                TextUtils.errorValidText(oldPasswordStr, getString(R.string.old_password))
                return@setOnClickListener
            }
            val newPasswordStr = newPassword.getMyEditText()
            if (PatternUtil.isNullOrEmpty(newPasswordStr.text.toString())) {
                TextUtils.errorEmptyText(newPasswordStr, getString(R.string.new_password))
                return@setOnClickListener
            }
            if (PatternUtil.isValidPassword(newPasswordStr.text.toString())) {
                TextUtils.errorValidText(newPasswordStr, getString(R.string.new_password))
                return@setOnClickListener
            }



            val confirmPasswordStr = confirmPassword.getMyEditText()
            if (PatternUtil.isNullOrEmpty(confirmPasswordStr.text.toString())) {
                TextUtils.errorEmptyText(confirmPasswordStr, getString(R.string.confirm_password))
                return@setOnClickListener
            }
            val passNew = newPasswordStr.text.toString().trim()
            val passConfirm = confirmPasswordStr.text.toString().trim()
            if (passNew !=passConfirm){
                TextUtils.errorNotMatchText(confirmPasswordStr, getString(R.string.new_password),getString(R.string.confirm_password))
                return@setOnClickListener
            }
            changePassword(oldPasswordStr.text.toString().trim(),passNew)


        }

        model.changePassword.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            val status = (it.status ?: C.NP_STATUS_FAIL)
            if (status == C.NP_INVALID_TOKEN || status == C.NP_TOKEN_EXPIRED || status == C.NP_TOKEN_NOT_FOUND) {
                SharedPreferencesUtils.setUserDetails(model.sharedPreferences, null)
                setResult(RESULT_OK)
                finish()
                return@observe
            }
            try {

                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS){
                    setResult(RESULT_OK)
                    finishAfterTransition()
                }else{
                    toast(it.message?:getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })
    }

    private fun changePassword(oldPass:String,newPass:String){
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.changePassword(oldPass, newPass)
        }
        else
            toast(R.string.no_internet_connection)
    }
    override fun layout(): Int = R.layout.activity_change_password

    override fun tag(): String = TAG

    companion object{
        private const val TAG = "ChangePasswordActivity:: "
    }

    override val title: String
        get() = getString(R.string.change_password)
    override val isShowTitle: Boolean
        get() = true
}