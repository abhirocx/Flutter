package com.np.namasteyoga.ui.searchCity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.CityListResponse
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.ConstUtility
import kotlinx.android.synthetic.main.activity_search_city.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ActivitySearchCity : AppCompatActivity() {
    private var adapterCities: AdapterCities? = null
    private var requestQueue: RequestQueue? = null
    private var citySelected: City? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_city)
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
        spinnerCity.onItemClickListener = OnItemClickListener { _, _, i, l ->
            etSelectCity.setText(adapterCities?.getItem(i)?.city)
            citySelected = adapterCities?.getItem(i)
            val intent = Intent()
            intent.putExtra(C.CITY, citySelected)
            setResult(RESULT_OK, intent)
            onBackPressed()
            ConstUtility.hideKeyBoard(this@ActivitySearchCity)
        }
        ivBack.setOnClickListener {
            onBackPressed()
            ConstUtility.hideKeyBoard(this@ActivitySearchCity)
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
                BuildConfig.BASE_URL+C.API_CITY_SEARCH, jsonObj,
                Response.Listener { response -> // Display the first 500 characters of the response string.
                    try {
                        val cityListResponse =
                            gson.fromJson(response.toString(), CityListResponse::class.java)
                        val cities = cityListResponse.data as ArrayList<City>
                        adapterCities = AdapterCities(this@ActivitySearchCity, cities)
                        spinnerCity.adapter = adapterCities
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error -> Log.e("App", error.toString()) }) {
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
            Toast.makeText(
                this, R.string.no_internet_connection,
                Toast.LENGTH_LONG
            ).show()
        }
    }


}