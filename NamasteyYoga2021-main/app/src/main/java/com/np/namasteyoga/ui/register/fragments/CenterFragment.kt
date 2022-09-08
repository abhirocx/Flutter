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
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.RegisterRequest
import com.np.namasteyoga.ui.map.MapActivity
import com.np.namasteyoga.ui.otpVerify.OTPVerifyActivity
import com.np.namasteyoga.ui.register.RegisterActivity
import com.np.namasteyoga.ui.searchCity.ActivitySearchCity
import com.np.namasteyoga.ui.termsAndConditions.TermsAndConditionsActivity
import com.np.namasteyoga.utils.*
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import kotlinx.android.synthetic.main.button_colored_layout.*
import kotlinx.android.synthetic.main.fragment_center.*
import kotlinx.android.synthetic.main.fragment_center.checkbox_terms_n_conditions
import kotlinx.android.synthetic.main.fragment_center.etAddress
import kotlinx.android.synthetic.main.fragment_center.etChooseCity
import kotlinx.android.synthetic.main.fragment_center.etConfirmPassword
import kotlinx.android.synthetic.main.fragment_center.etEmailId
import kotlinx.android.synthetic.main.fragment_center.etLocation
import kotlinx.android.synthetic.main.fragment_center.etMobile
import kotlinx.android.synthetic.main.fragment_center.etPassword
import kotlinx.android.synthetic.main.fragment_center.tvLogin
import kotlinx.android.synthetic.main.fragment_center.tv_terms_n_conditions
import org.jetbrains.anko.support.v4.toast
import java.text.DecimalFormat

class CenterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        etPassword.getMyEditText().transformationMethod = PasswordTransformationMethod()
        etConfirmPassword.getMyEditText().transformationMethod = PasswordTransformationMethod()
        etPassword.getMyImageView().setOnClickListener { etPassword.updatePasswordVisibility() }
        etConfirmPassword.getMyImageView()
            .setOnClickListener { etConfirmPassword.updatePasswordVisibility() }
        etCenterName.manageEditText(
            InputType.TYPE_TEXT_VARIATION_PERSON_NAME, CommonInt.NameLength, getString(
                R.string.center_name
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

        etSittingCapacity.manageEditText(
            InputType.TYPE_CLASS_NUMBER,
            CommonInt.SittingCapacityLength,
            getString(R.string.sitting_capacity)
        )

        etLocation.manageEditText(
            InputType.TYPE_CLASS_TEXT,
            CommonInt.NormalLength,
            getString(R.string.get_location)
        )
        etAddress.manageEditText(
            InputType.TYPE_CLASS_TEXT,
            CommonInt.AddressLength,
            getString(R.string.address)
        )
        etChooseCity.manageEditText(
            InputType.TYPE_CLASS_TEXT,
            CommonInt.AddressLength,
            getString(R.string.chooseCity)
        )
        buttonColorLayoutTxt.setText(R.string.register_2)

        etLocation.getMyEditText().setReadOnly(true)
        etChooseCity.getMyEditText().setReadOnly(true)

        etLocation.setImage(R.drawable.ic_location_gray)
        etAddress.setImage(R.drawable.ic_location_gray)
        etChooseCity.setImage(R.drawable.ic_location_gray)

        etChooseCity.getMyEditText().setOnClickListener {
            selectCityPopup()
        }
        etLocation.getMyEditText().setOnClickListener {
            openPlacePicker()
        }
        buttonColorLayout.setOnClickListener {
            val trainer = etCenterName.getMyEditText()
            if (PatternUtil.isNullOrEmpty(trainer.text.toString())) {
                TextUtils.errorEmptyText(trainer, getString(R.string.center_name))
                return@setOnClickListener
            }
            if (PatternUtil.isValidName(trainer.text.toString())) {
                TextUtils.errorValidText(trainer, getString(R.string.center_name))
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
            val mobile = etMobile.getMyEditText()
            if (PatternUtil.isNullOrEmpty(mobile.text.toString())) {
                TextUtils.errorEmptyText(mobile, getString(R.string.mobile_number))
                return@setOnClickListener
            }
            if (PatternUtil.isValidMobile(mobile.text.toString())) {
                TextUtils.errorValidText(mobile, getString(R.string.mobile_number))
                return@setOnClickListener
            }

            val capacity = etSittingCapacity.getMyEditText()
            if (PatternUtil.isNullOrEmpty(capacity.text.toString())) {
                TextUtils.errorEmptyText(capacity, getString(R.string.sitting_capacity))
                return@setOnClickListener
            }

            var tempCapacity = 0
            val validCapacity =
                try {
                    tempCapacity = capacity.text.toString().toInt()
                    when (tempCapacity) {
                        in 1..10000 -> {

                            false
                        }
                        else -> {
                            true
                        }
                    }
                } catch (e: Exception) {
                    true
                }

            if (validCapacity){
                TextUtils.capacityValidationMsg(capacity)
                return@setOnClickListener
            }

            val location = etLocation.getMyEditText()
            if (PatternUtil.isNullOrEmpty(location.text.toString())) {
                TextUtils.errorEmptyText(location, getString(R.string.select_location))
                return@setOnClickListener
            }

            val _address = etAddress.getMyEditText()

            if (PatternUtil.isNullOrEmpty(_address.text.toString())) {
                TextUtils.errorEmptyText(_address, getString(R.string.address))
                return@setOnClickListener
            }
            if (PatternUtil.isValidAddress(_address.text.toString())) {
                TextUtils.errorValidText(_address, getString(R.string.address))
                return@setOnClickListener
            }

            if (citySelected == null) {
                TextUtils.errorValidText(
                    etChooseCity.getMyEditText(),
                    getString(R.string.select_city)
                )
                return@setOnClickListener
            }
            if (!checkbox_terms_n_conditions.isChecked) {
                toast(R.string.terms_and_conditions_msg)
                return@setOnClickListener
            }

            val distance = latLng?.run {
                val latLngDouble = LatLng(
                    citySelected?.lat?.toDouble() ?: 0.0,
                    citySelected?.lng?.toDouble() ?: 0.0
                )
                Util.distance(this, latLngDouble)
            }



            var addressStr: String = _address.text.toString().trim()
            addressStr = addressStr.replace("/", "__")


            val registerRequest = RegisterRequest().apply {
                nearest_distance = Util.formatKM(distance?:0f)
                name = trainer.text.toString().trim().getCapitalized()
                email = _email.text.toString().trim().toLowerCase()
                password = passNew
                passwordConfirmation = passNew
                roleId = UserRoleId
                zip = ZIP
                phone = mobile.text.toString().trim()
                nearest = nearestCityVar
                lat = latitude.toString()
                lng = longitude.toString()
                address = addressStr
                sitting_capacity = tempCapacity
                city = citySelected?.city
                state_name = citySelected?.state_name
                country_name = citySelected?.country_name

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
    }

    private fun startOTPVerifyActivity() {
        val intent = Intent(context, OTPVerifyActivity::class.java)
        intent.putExtra(IntentUtils.OTP_KEY, latLng)
        startActivityForResult(intent, CommonInt.hundred)
    }

    private fun startTermsAndConditionsActivity() {
        val intent = Intent(context, TermsAndConditionsActivity::class.java)
        intent.putExtra(IntentUtils.SHARE_STRING,TermsAndConditionsActivity.TERMS)
        startActivity(intent)
    }

    var nearestCityVar = 0
    private fun selectCityPopup() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_select_city)
        val currentCity = dialog.findViewById<View>(R.id.btnCurrentCity) as RelativeLayout
        val nearestCity = dialog.findViewById<View>(R.id.btnNearestCity) as RelativeLayout
        val ivCross = dialog.findViewById<View>(R.id.ivCross) as ImageView
        currentCity.setOnClickListener {
            nearestCityVar = 0
            dialog.dismiss()
            val intent = Intent(activity, ActivitySearchCity::class.java)
            startActivityForResult(
                intent,
                CITY_PICKER_REQUEST
            )
        }
        nearestCity.setOnClickListener {
            nearestCityVar = 1
            dialog.dismiss()
            val intent = Intent(activity, ActivitySearchCity::class.java)
            startActivityForResult(
                intent,
                CITY_PICKER_REQUEST
            )
        }
        ivCross.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    var latitude = 0.0
    var longitude = 0.0
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OTPKey -> {
                    if (data != null && data.hasExtra(IntentUtils.key)) {
                        latLng = data.getParcelableExtra(IntentUtils.key)
                        if (latLng != null) {
                            latitude = latLng?.latitude ?: 0.0
                            longitude = latLng?.longitude ?: 0.0
//                            val lat = "" + latLng?.latitude + "," + latLng?.longitude
                            val df = DecimalFormat(CommonString.LAT_FORMAT)
                            val lat =
                                "${df.format(latLng?.latitude)},${df.format(latLng?.longitude)}"
                            etLocation.getMyEditText().setText(lat)
                        }
                    }
                }
                CommonInt.hundred -> {
                    activity?.finishAfterTransition()
                }
                CITY_PICKER_REQUEST -> {
                    if (data != null && data.hasExtra(C.CITY)) {
                        citySelected = data.getSerializableExtra(C.CITY) as City?
                        etChooseCity.getMyEditText().setText(citySelected?.city)
                    }
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private var latLng: LatLng? = null
    var citySelected: City? = null
    private fun openPlacePicker() {
        val intent = Intent(context, MapActivity::class.java)
        intent.putExtra(IntentUtils.OTP_KEY, latLng)
        startActivityForResult(intent, OTPKey)
    }

    companion object {
        private const val TAG = "CenterFragment ::: "
        private const val LatLongKey = 1115
        private const val OTPKey = 1116
        private const val CITY_PICKER_REQUEST = 11111
        private const val UserRoleId = 2
        private const val ZIP = "312323"
    }
}