package com.np.namasteyoga.ui.otpVerify

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.content.ContextCompat
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.RegisterRequest
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_o_t_p_verify.bigBack
import kotlinx.android.synthetic.main.activity_o_t_p_verify.etOTP
import kotlinx.android.synthetic.main.activity_o_t_p_verify.resendOTP
import kotlinx.android.synthetic.main.button_colored_layout.*
import org.jetbrains.anko.toast

class OTPVerifyActivity : BaseActivity<OTPVerifyViewModel>(OTPVerifyViewModel::class) {

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
    private var registerRequest: RegisterRequest? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.run {
            if (intent.hasExtra(IntentUtils.SHARE_USER_DETAILS)) {
                registerRequest = intent.getSerializableExtra(IntentUtils.SHARE_USER_DETAILS) as RegisterRequest?
            }
        }
        buttonColorLayoutTxt.setText(R.string.submit)
        bigBack.setOnClickListener {
            finish()
        }
        resendOTP.setOnClickListener {
            register()
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
            otpVerify(otp.text.toString().trim())

        }

        model.responseRegister.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {

                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    countDownTimer.start()
                   val msg= if(it.message.isNullOrEmpty()){
                        getString(R.string.resend_otp_msg)
                    }else{
                        it.message
                    }
                    toast(msg)
                }else{
                    toast(it.message ?: getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })

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
                    finishAfterTransition()
                } else {
                    toast(it.message ?:getString(R.string.something_went_wrong))
                }
                Logger.Debug(it.data?.phone.toString())
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })
        model.responseOTP.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    setShowAlert()

                } else {
                    toast(it.message ?:getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })
    }

    private fun setShowAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.after_otp)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                setResult(RESULT_OK)
                finishAfterTransition()
            }
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark))
    }

    private fun login() {
        if (registerRequest == null) {
            toast(R.string.something_went_wrong)
            return
        }
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.login(registerRequest?.email, registerRequest?.password)
        } else
            toast(R.string.no_internet_connection)
    }

    private fun register() {
        if (registerRequest == null) {
            toast(R.string.something_went_wrong)
            return
        }
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.register(registerRequest!!)
        } else
            toast(R.string.no_internet_connection)
    }

    private fun otpVerify(otp: String) {
        if (registerRequest == null) {
            toast(R.string.something_went_wrong)
            return
        }
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.otpVerify(registerRequest!!, otp)
        } else
            toast(R.string.no_internet_connection)
    }

    override fun layout(): Int = R.layout.activity_o_t_p_verify

    override fun tag(): String = TAG

    companion object {
        private const val TAG = "OTPVerifyActivity:: "
    }

    override val title: String
        get() = CommonString.Empty
    override val isShowTitle: Boolean
        get() = false
}