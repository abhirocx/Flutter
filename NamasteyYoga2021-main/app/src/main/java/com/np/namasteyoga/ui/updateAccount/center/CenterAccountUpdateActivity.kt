package com.np.namasteyoga.ui.updateAccount.center

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.EditRequest
import com.np.namasteyoga.ui.map.MapActivity
import com.np.namasteyoga.ui.searchCity.ActivitySearchCity
import com.np.namasteyoga.utils.*
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import kotlinx.android.synthetic.main.activity_center_account_update.bigBack
import kotlinx.android.synthetic.main.activity_center_account_update.etAddress
import kotlinx.android.synthetic.main.activity_center_account_update.etCenterName
import kotlinx.android.synthetic.main.activity_center_account_update.etChooseCity
import kotlinx.android.synthetic.main.activity_center_account_update.etEmailId
import kotlinx.android.synthetic.main.activity_center_account_update.etLocation
import kotlinx.android.synthetic.main.activity_center_account_update.etMobile
import kotlinx.android.synthetic.main.activity_center_account_update.etSittingCapacity
import kotlinx.android.synthetic.main.button_colored_layout.*
import org.jetbrains.anko.toast

class CenterAccountUpdateActivity : BaseActivity<CenterAccountUpdateViewModel>(CenterAccountUpdateViewModel::class) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        bigBack.setOnClickListener {
            finish()
        }

        etCenterName.manageAccountEditText(
            InputType.TYPE_TEXT_VARIATION_PERSON_NAME, CommonInt.NameLength, getString(
                R.string.center_name
            )
        )

        etMobile.manageAccountEditText(
            InputType.TYPE_CLASS_NUMBER,
            CommonInt.MobileNoLength,
            getString(R.string.mobile_number)
        )
        etSittingCapacity.manageAccountEditText(
            InputType.TYPE_CLASS_NUMBER,
            CommonInt.SittingCapacityLength,
            getString(R.string.sitting_capacity)
        )

        etLocation.manageAccountEditText(
            InputType.TYPE_CLASS_TEXT,
            CommonInt.NormalLength,
            getString(R.string.get_location),
            R.drawable.ic_location_color
        )
        etAddress.manageAccountEditText(
            InputType.TYPE_CLASS_TEXT,
            CommonInt.AddressLength,
            getString(R.string.address),
            R.drawable.ic_location_color
        )
        etChooseCity.manageAccountEditText(
            InputType.TYPE_CLASS_TEXT,
            CommonInt.AddressLength,
            getString(R.string.chooseCity),
            R.drawable.ic_location_color
        )
        buttonColorLayoutTxt.setText(R.string.update)

        etLocation.getMyEditText().setReadOnly(true)
        etChooseCity.getMyEditText().setReadOnly(true)
        etEmailId.getMyEditText().setReadOnly(true)
//        etLocation.setImage(R.drawable.ic_location_gray)
//        etAddress.setImage(R.drawable.ic_location_gray)
//        etChooseCity.setImage(R.drawable.ic_location_gray)

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


            val distanceTemp = latLng?.run {
                val latLngDouble = LatLng(
                    citySelected?.lat?.toDouble() ?: 0.0,
                    citySelected?.lng?.toDouble() ?: 0.0
                )
                Util.distance(this, latLngDouble)
            }


            var addressStr: String = _address.text.toString().trim()
            addressStr = addressStr.replace("/", "__")

            val distance = if (isLocationUpdate) {
                Util.formatKM(distanceTemp?:0f)
            } else {
                model.userDetail.value?.nearest_distance.toString()
            }

            val request = EditRequest().apply {
                nearest_distance = distance
                address = addressStr
                name = trainer.text.toString().trim().getCapitalized()
                phone = mobile.text.toString().trim()
                nearest = nearestCityVar
                lat = (latitude ?: 0.0).toString()
                lng = (longitude ?: 0.0).toString()
                roleId = UserRoleId
                sitting_capacity = tempCapacity
                city = citySelected?.city
                state = citySelected?.state_name
                country = citySelected?.country_name
                zip = model.userDetail.value?.zip.toString()
            }
            updateAccount(request)


        }
        model.userDetail.observe(this, Observer {
            it?.run {
                etMobile.getMyEditText().setText(phone?:CommonString.Empty)
                etSittingCapacity.getMyEditText().setText(sitting_capacity?.toString()?:CommonString.Zero)
                etCenterName.getMyEditText().run {
                    val str = (name ?: CommonString.Empty).getCapitalized()
                    setText(str)
                    setSelection(str.length)
                }
                etAddress.getMyEditText().setText(address?:CommonString.Empty)
                etChooseCity.getMyEditText().setText(cityName?:CommonString.Empty)
                etLocation.getMyEditText().setText("${lat?:0.0},${lng?:0.0}")
                etEmailId.setMyAccountDataEdit(getString(R.string.email_id),email ?: CommonString.NA)

                nearestCityVar = nearest ?: CommonInt.Zero
                citySelected = City().also { a ->
                    a.city = cityName
                    a.state_name = stateName
                    a.country_name = countryName
                    // a.l
                }
                latLng = LatLng(lat ?: 0.0, lng ?: 0.0)
                latitude = lat ?: 0.0
                longitude = lng ?: 0.0
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

    var nearestCityVar = 0
    private fun selectCityPopup() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_select_city)
        val currentCity = dialog.findViewById<View>(R.id.btnCurrentCity) as RelativeLayout
        val nearestCity = dialog.findViewById<View>(R.id.btnNearestCity) as RelativeLayout
        val ivCross = dialog.findViewById<View>(R.id.ivCross) as ImageView
        currentCity.setOnClickListener {
            nearestCityVar = 0
            dialog.dismiss()
            val intent = Intent(this@CenterAccountUpdateActivity, ActivitySearchCity::class.java)
            startActivityForResult(
                intent,
                CITY_PICKER_REQUEST
            )
        }
        nearestCity.setOnClickListener {
            nearestCityVar = 1
            dialog.dismiss()
            val intent = Intent(this@CenterAccountUpdateActivity, ActivitySearchCity::class.java)
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
    private var isLocationUpdate = false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OTPKey -> {
                    if (data != null && data.hasExtra(IntentUtils.key)) {
                        latLng = data.getParcelableExtra(IntentUtils.key)
                        if (latLng != null) {
                            latitude = latLng?.latitude ?: 0.0
                            longitude = latLng?.longitude ?: 0.0
                            val lat = "" + latLng?.latitude + "," + latLng?.longitude
                            etLocation.getMyEditText().setText(lat)
                            etChooseCity.getMyEditText().text = null
                            citySelected = null
                            isLocationUpdate = true
                        }
                    }
                }
                CommonInt.hundred -> {
                   finishAfterTransition()
                }
                CITY_PICKER_REQUEST -> {
                    if (data != null && data.hasExtra(C.CITY)) {
                        citySelected = data.getSerializableExtra(C.CITY) as City?
                        etChooseCity.getMyEditText().setText(citySelected?.city)
                        isLocationUpdate = true
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
        intent.putExtra(IntentUtils.key, latLng)
        startActivityForResult(intent, OTPKey)
    }

    override fun layout(): Int = R.layout.activity_center_account_update

    override fun tag(): String = TAG

    companion object{
        private const val TAG = "CenterAccountUpdateActivity:: "
        private const val OTPKey = 1116
        private const val CITY_PICKER_REQUEST = 11111
        private const val UserRoleId = 2
    }

    override val title: String
        get() = CommonString.Empty
    override val isShowTitle: Boolean
        get() = false
}