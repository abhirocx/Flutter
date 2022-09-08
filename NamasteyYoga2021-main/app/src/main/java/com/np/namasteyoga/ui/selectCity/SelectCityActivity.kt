package com.np.namasteyoga.ui.selectCity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.CityListResponse
import com.np.namasteyoga.services.LocationAddress
import com.np.namasteyoga.ui.health.main.FitActionRequestCode
import com.np.namasteyoga.ui.searchCity.AdapterCities
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.ConstUtility
import com.np.namasteyoga.utils.Logger
import kotlinx.android.synthetic.main.activity_city_search_home.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SelectCityActivity :BaseActivity<SelectCityViewModel>(SelectCityViewModel::class) {


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var adapterCities:AdapterCities?=null
    //   private ProgressDialog progressDialog;
    private var requestQueue: RequestQueue? = null
    private var citySelected: City? = null
    private var showToast = false
    private var isClicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        ivBack.setOnClickListener {
            finishAfterTransition()
        }
        llCurrentLocation.setOnClickListener {
            showToast = true
            isClicked = true
            checkPermissionsAndRun(FitActionRequestCode.SUBSCRIBE)
        }
        etSelectCity.requestFocus()
        etSelectCity.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                println("Text [$s]")
                if (s.isNotEmpty()) searchCity(s.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        })
        spinnerCity.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, l ->
            etSelectCity.setText(adapterCities?.getItem(i)?.city)
            citySelected = adapterCities?.getItem(i)
            val intent = Intent()
            intent.putExtra(C.CITY, citySelected)
            setResult(RESULT_OK, intent)
            onBackPressed()
            ConstUtility.hideKeyBoard(this@SelectCityActivity)
        }
        checkPermissionsAndRun(FitActionRequestCode.SUBSCRIBE)
    }

    private fun getLocation(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (showToast){
                toast(R.string.default_permission_msg_location)
            }
            return
        }
        if (!ConstUtility.isGpsEnabled(this@SelectCityActivity)) {
            if (showToast){
                ConstUtility.showSettingsAlert(this@SelectCityActivity)
            }
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.run {
                    Logger.Debug(location.toString())
                    val locationAddress = LocationAddress()
                    LocationAddress.getAddressFromLocation(
                        latitude, longitude,
                        applicationContext, GeoCodeHandler()
                    )
                }
            }

    }

    private fun searchCity(cityInitials: String) {
        if (ConstUtility.isNetworkConnectivity(this)) {
            val hashMap = HashMap<String, String>()
            hashMap["search"] = cityInitials
            val gson = Gson()
            val json = gson.toJson(hashMap)
            var jsonObj: JSONObject? = null
            try {
                jsonObj = JSONObject(json)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val req: JsonObjectRequest = object : JsonObjectRequest(
                BuildConfig.BASE_URL+C.API_CITY_SEARCH,
                jsonObj,
                Response.Listener { response ->
                    // Display the first 500 characters of the response string.
                    try {
                        //    progressDialog.dismiss();
                        val cityListResponse: CityListResponse = gson.fromJson(
                            response.toString(),
                            CityListResponse::class.java
                        )
                        adapterCities = AdapterCities(
                            this@SelectCityActivity,
                            cityListResponse.data
                        )
                        spinnerCity.adapter = adapterCities
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error -> Log.e("App", error.toString()) }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    return ConstUtility.getHeaderCity(json)
                }
            }
            val policy: RetryPolicy = DefaultRetryPolicy(
                C.API_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            req.retryPolicy = policy
            requestQueue = Volley.newRequestQueue(this)
            requestQueue?.add(req)
        } else {
            toast(R.string.no_internet_connection)
        }
    }


    private fun checkPermissionsAndRun(fitActionRequestCode: FitActionRequestCode) {
        if (permissionApproved()) {

            if (ConstUtility.isGpsEnabled(context)) {
                getLocation()
            } else {
                ConstUtility.showSettingsAlert(this)
            }
        } else {
            showPermissionPopup(fitActionRequestCode)

        }
    }

    private fun showPermissionPopup(fitActionRequestCode: FitActionRequestCode) {
        try {
            val alertBuilder = AlertDialog.Builder(context)
            alertBuilder.setCancelable(false)
            alertBuilder.setTitle(R.string.permission_title)
            alertBuilder.setMessage(R.string.default_permission_msg_location)
            alertBuilder.setPositiveButton(
                R.string.ok
            ) { dialog, _ ->
                dialog.dismiss()
                requestRuntimePermissions(fitActionRequestCode)
            }
            val alert = alertBuilder.create()
            alert.show()
        } catch (e: Exception) {
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when {
            grantResults.isEmpty() -> {
                Logger.Debug(tag = TAG, msg = "User interaction was cancelled.")
            }
            grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                if (ConstUtility.isGpsEnabled(context)) {
                    getLocation()
                } else {
                    ConstUtility.showSettingsAlert(this)
                }
            }
            else -> {

                Snackbar.make(
                    find(R.id.main_activity_view),
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.settings) {
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            C.APPLICATION_ID, null
                        )
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .show()
            }
        }
    }

    private val runningMOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    private fun permissionApproved(): Boolean {
        return if (runningMOrLater) {
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            true
        }
    }

    private fun requestRuntimePermissions(requestCode: FitActionRequestCode) {
        val shouldProvideRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        requestCode.let {
            if (shouldProvideRationale) {
                Logger.Debug(
                    tag = TAG,
                    msg = "Displaying permission rationale to provide additional context."
                )
                Snackbar.make(
                    find(R.id.main_activity_view),
                    R.string.permission_rationale_splash,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.ok) {
                        // Request permission
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            requestCode.ordinal
                        )
                    }
                    .show()
            } else {
                Logger.Debug(tag = TAG, msg = "Requesting permission")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    requestCode.ordinal
                )
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        checkPermissionsAndRun(FitActionRequestCode.SUBSCRIBE)
    }


    private var city: City? = null
    internal inner class GeoCodeHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            val locationAddress: String
            locationAddress = when (message.what) {
                1 -> {
                    val bundle = message.data
                    val tempCity = City().apply {
                        city = bundle.getString(C.CITY) ?: CommonString.Empty
                        state_name = bundle.getString(C.STATE) ?: CommonString.Empty
                        country_name = bundle.getString(C.COUNTRY) ?: CommonString.Empty
                    }
                    city = tempCity
                    bundle.getString("address") ?: CommonString.Empty

                }
                else -> null.toString()
            }
            city?.run {
                getSelectCity()
            }
            Logger.Debug(locationAddress)
        }
    }

    private fun getSelectCity() {
        if (city!=null && isClicked){
            val intent = Intent()
            intent.putExtra(C.CITY, city)
            setResult(RESULT_OK, intent)
            onBackPressed()
        }
    }

    companion object{
        private const val TAG = "SelectCityActivity:::  "
    }

    override fun layout(): Int = R.layout.activity_city_search_home

    override fun tag(): String = TAG

    override val title: String
        get() = CommonString.Empty
    override val isShowTitle: Boolean
        get() = false
}