package com.np.namasteyoga.ui.register.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.datasource.pojo.RegisterRequest
import com.np.namasteyoga.ui.otpVerify.OTPVerifyActivity
import com.np.namasteyoga.ui.register.RegisterActivity
import com.np.namasteyoga.ui.searchCity.ActivitySearchCity
import com.np.namasteyoga.ui.termsAndConditions.TermsAndConditionsActivity
import com.np.namasteyoga.utils.*
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import kotlinx.android.synthetic.main.button_colored_layout.*
import kotlinx.android.synthetic.main.fragment_center.*
import kotlinx.android.synthetic.main.fragment_guest_user.*
import kotlinx.android.synthetic.main.fragment_guest_user.checkbox_terms_n_conditions
import kotlinx.android.synthetic.main.fragment_guest_user.etConfirmPassword
import kotlinx.android.synthetic.main.fragment_guest_user.etEmailId
import kotlinx.android.synthetic.main.fragment_guest_user.etMobile
import kotlinx.android.synthetic.main.fragment_guest_user.etPassword
import kotlinx.android.synthetic.main.fragment_guest_user.tvLogin
import kotlinx.android.synthetic.main.fragment_guest_user.tv_terms_n_conditions
import org.jetbrains.anko.support.v4.toast

class GuestUserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_guest_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        etPassword.getMyEditText().transformationMethod = PasswordTransformationMethod()
        etConfirmPassword.getMyEditText().transformationMethod = PasswordTransformationMethod()
        etPassword.getMyImageView().setOnClickListener { etPassword.updatePasswordVisibility() }
        etConfirmPassword.getMyImageView()
            .setOnClickListener { etConfirmPassword.updatePasswordVisibility() }
        etFullName.manageEditText(
            InputType.TYPE_TEXT_VARIATION_PERSON_NAME, CommonInt.NameLength, getString(
                R.string.full_name
            )
        )
        etEmailId.manageEditText(
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, CommonInt.EmailIdLength, getString(
                R.string.email_id
            )
        )
        etPassword.manageEditText(
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD, CommonInt.PasswordLength, getString(
                R.string.password
            )
        )
        etConfirmPassword.manageEditText(
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD, CommonInt.PasswordLength, getString(
                R.string.confirm_password
            )
        )

        etMobile.manageEditText(
            InputType.TYPE_CLASS_NUMBER,
            CommonInt.MobileNoLength,
            getString(R.string.mobile_number)
        )

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


            val _email = etEmailId.getMyEditText()
            if (PatternUtil.isNullOrEmpty(_email.text.toString())) {
                TextUtils.errorEmptyText(_email, getString(R.string.email_id))
                return@setOnClickListener
            }
            if (PatternUtil.isValidEmail(_email.text.toString())) {
                TextUtils.errorValidText(_email, getString(R.string.email_id))
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
            val pwd = etPassword.getMyEditText()
            if (PatternUtil.isNullOrEmpty(pwd.text.toString())) {
                TextUtils.errorEmptyText(pwd, getString(R.string.password))
                return@setOnClickListener
            }
            if (PatternUtil.isValidPassword(pwd.text.toString())) {
                TextUtils.passwordValidationMsg(pwd)
                return@setOnClickListener
            }
            val confirmPasswordStr = etConfirmPassword.getMyEditText()
            if (PatternUtil.isNullOrEmpty(confirmPasswordStr.text.toString())) {
                TextUtils.errorEmptyText(confirmPasswordStr, getString(R.string.confirm_password))
                return@setOnClickListener
            }
            val passNew = pwd.text.toString().trim()
            val passConfirm = confirmPasswordStr.text.toString().trim()
            if (passNew != passConfirm) {
                TextUtils.errorNotMatchText(
                    confirmPasswordStr, getString(R.string.password), getString(
                        R.string.confirm_password
                    )
                )
                return@setOnClickListener
            }

            if (!checkbox_terms_n_conditions.isChecked) {
                toast(R.string.terms_and_conditions_msg)
                return@setOnClickListener
            }
            val registerRequest = RegisterRequest().apply {
                name = trainer.text.toString().trim().getCapitalized()
                email = _email.text.toString().trim().toLowerCase()
                password = passNew
                passwordConfirmation = passNew
                roleId = UserRoleId
                phone = mobile.text.toString().trim()

            }

            (activity as RegisterActivity).register(registerRequest)
        }
        tv_terms_n_conditions.setOnClickListener {
            if (NetworkUtil.isInternetAvailable(requireContext()))
                startTermsAndConditionsActivity()
            else{
                toast(R.string.no_internet_connection)
            }
        }
        tvLogin.setOnClickListener {
            activity?.finish()
        }
        buttonColorLayoutTxt.setText(R.string.register_2)
    }

    private val latLng: LatLng? = null



    private fun startOTPVerifyActivity() {
        val intent = Intent(context, OTPVerifyActivity::class.java)
        intent.putExtra(IntentUtils.OTP_KEY, latLng)
        startActivityForResult(intent, CommonInt.hundred)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CommonInt.hundred -> {
                    activity?.finishAfterTransition()
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startTermsAndConditionsActivity() {
        val intent = Intent(context, TermsAndConditionsActivity::class.java)
        intent.putExtra(IntentUtils.SHARE_STRING,TermsAndConditionsActivity.TERMS)
        startActivity(intent)
    }

    companion object {
        var nearestCityVar = 0
        private const val TAG = "GuestUserFragment ::: "
        private const val LatLongKey = 1115
        private const val CITY_PICKER_REQUEST = 11111
        private const val UserRoleId = 5

    }
}