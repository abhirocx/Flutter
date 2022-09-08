package com.np.namasteyoga.ui.trainer.trainerList

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonBoolean
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.response.User
import com.np.namasteyoga.dialogs.MyRoundedBottomSheet
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.services.LocationAddress
import com.np.namasteyoga.ui.trainer.trainerDetails.CustomDialogFragment
import com.np.namasteyoga.ui.trainer.trainerList.adapter.TrainerListAdapter
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_trainer_list.*
import kotlinx.android.synthetic.main.button_colored_layout.*
import kotlinx.android.synthetic.main.toolbar_black_with_back_and_img.*
import org.jetbrains.anko.toast


class TrainerListActivity : BaseActivity<TrainerListViewModel>(TrainerListViewModel::class),
    OnClickItem<User>, PaginationListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val list = ArrayList<User>()
    private var latLng:LatLng?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        list.clear()
        settings.setOnClickListener {
            startSearchTrainerListActivity()
        }
        buttonColorLayout.setOnClickListener {
            startLoginActivity()
        }
        buttonColorLayoutTxt.setText(R.string.register_login)
        count.text = getString(R.string.name_count, getString(R.string.yoga_trainers), 0)

        val lat = LatLng(Util.citySelected?.lat?.toDouble()?:0.0, Util.citySelected?.lng?.toDouble()?:0.0)
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@TrainerListActivity)
            adapter = TrainerListAdapter(
                list,
                this@TrainerListActivity,
                this@TrainerListActivity,
                lat
            )
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            finish()
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.run {
                    Logger.Debug(location.toString())
                    val locationAddress = LocationAddress()
                    val lat = LatLng(latitude, longitude)
                    LocationAddress.getAddressFromLocation(
                        latitude, longitude,
                        applicationContext, GeoCodeHandler()
                    )
                    latLng = lat

                }
            }

        model.response.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {
//                toast(it.message ?: getString(R.string.something_went_wrong))
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    if (it.data?.isNotEmpty() == true) {
                        it.data.run {
                            list.addAll(this)
                        }
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                    if (list.isNotEmpty())
                        noRecordFound.hide()

                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })

        model.token.observe(this, Observer {
            if (it!=null)
                buttonColorLayout.hide()
            else
                buttonColorLayout.show()
        })
        city = Util.citySelected
        getUserList()
    }

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
//            city?.run {
//                getUserList()
//            }
            Logger.Debug(locationAddress)
        }
    }

    private var city: City? = null
    private fun getUserList() {
        if (city == null) {
            toast(R.string.something_went_wrong)
            return
        }
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            city?.run {
                model.getListWithPagination(this)
            }
        } else
            toast(R.string.no_internet_connection)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        model.getIsLogin()
    }

    override fun layout(): Int = R.layout.activity_trainer_list

    override fun tag(): String = TAG

    companion object {
        private const val TAG = "TrainerListActivity::: "
    }

    override val title: String
        get() = getString(R.string.yoga_trainers)
    override val isShowTitle: Boolean
        get() = CommonBoolean.TRUE

//    val myRoundedBottomSheet = MyRoundedBottomSheet()
    private val myRoundedBottomSheet = CustomDialogFragment()

    override fun onClick(position: Int, t: User?) {
//        t?.run {
//            if (myRoundedBottomSheet.isAdded || myRoundedBottomSheet.isVisible) {
//                myRoundedBottomSheet.dismiss()
//            }
//            myRoundedBottomSheet.show(supportFragmentManager, myRoundedBottomSheet.tag)
//
//            Handler(Looper.getMainLooper()).postDelayed({
//                latLng?.let {
//                    myRoundedBottomSheet.setData(this,it)
//                }
//            },300)
//        }
    }

    override fun page() {
        if (model.response.value?.current_page == model.response.value?.last_page)
            return
        getUserList()
    }
}