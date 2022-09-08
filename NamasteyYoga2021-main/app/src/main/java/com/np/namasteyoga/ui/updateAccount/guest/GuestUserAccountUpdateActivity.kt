package com.np.namasteyoga.ui.updateAccount.guest

import android.os.Bundle
import android.text.InputType
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.EditRequest
import com.np.namasteyoga.utils.*
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import kotlinx.android.synthetic.main.activity_guest_user_account_update.*
import kotlinx.android.synthetic.main.activity_guest_user_account_update.bigBack
import kotlinx.android.synthetic.main.activity_guest_user_account_update.etEmailId
import kotlinx.android.synthetic.main.activity_guest_user_account_update.etMobile
import kotlinx.android.synthetic.main.button_colored_layout.*
import org.jetbrains.anko.toast

class GuestUserAccountUpdateActivity : BaseActivity<GuestUserAccountUpdateViewModel>(GuestUserAccountUpdateViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        bigBack.setOnClickListener {
            finish()
        }
        etFullName.manageAccountEditText(
            InputType.TYPE_TEXT_VARIATION_PERSON_NAME, CommonInt.NameLength, getString(
                R.string.full_name
            )
        )
        etEmailId.getMyEditText().setReadOnly(true)
        etMobile.manageAccountEditText(
            InputType.TYPE_CLASS_NUMBER,
            CommonInt.MobileNoLength,
            getString(R.string.mobile_number)
        )
        etEmailId.getMyEditText().setTextColor(ContextCompat.getColor(context,R.color.hint_text_color))
        buttonColorLayoutTxt.setText(R.string.update)
        buttonColorLayout.setOnClickListener {
            val trainer = etFullName.getMyEditText()
            if (PatternUtil.isNullOrEmpty(trainer.text.toString())) {
                TextUtils.errorEmptyText(trainer, getString(R.string.full_name))
                return@setOnClickListener
            }
            if (PatternUtil.isValidName(trainer.text.toString())) {
                TextUtils.errorValidText(trainer, getString(R.string.full_name))
                return@setOnClickListener
            }
            val mobile = etMobile.getMyEditText()
            if (PatternUtil.isNullOrEmpty(mobile.text.toString())) {
                TextUtils.errorEmptyText(mobile, getString(R.string.mobile_number))
                return@setOnClickListener
            }
            if (PatternUtil.isValidMobile(mobile.text.toString())) {
                TextUtils.errorValidText(mobile, getString(R.string.mobile_number))
                return@setOnClickListener
            }

            val request = EditRequest().apply {
                name = trainer.text.toString().trim().getCapitalized()
                phone = mobile.text.toString().trim()
                roleId = UserRoleId
            }
            updateAccount(request)
        }

        model.userDetail.observe(this, Observer {
            it?.run {
                etMobile.getMyEditText().setText(phone?:CommonString.Empty)
                etFullName.getMyEditText().run {
                    val str = (name ?: CommonString.Empty).getCapitalized()
                    setText(str)
                    setSelection(str.length)
                }
                etEmailId.setMyAccountDataEdit(getString(R.string.email_id),email ?: CommonString.NA)
            }
        })

        model.response.observe(this, Observer {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@Observer
            }
            try {


                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    it.data?.run {
                        token = model.userDetail.value?.token
                    }
                    SharedPreferencesUtils.setUserDetails(model.sharedPreferences, it.data)
                    setResult(RESULT_OK)
                    finish()
                }  else {
                    toast(it.message ?: getString(R.string.something_went_wrong))
                }
                Logger.Debug(it.data?.phone.toString())
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })
    }
    private fun updateAccount(request: EditRequest) {
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.updateAccount(request)
        } else
            toast(R.string.no_internet_connection)
    }

    override fun layout(): Int = R.layout.activity_guest_user_account_update

    override fun tag(): String = TAG

    companion object{
        private const val TAG ="GuestUserAccountUpdateActivity:: "
        private const val UserRoleId = 5
    }

    override val title: String
        get() =CommonString.Empty
    override val isShowTitle: Boolean
        get() = false
}