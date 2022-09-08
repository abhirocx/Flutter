package com.np.namasteyoga.ui.trainer.searchtrainer

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxbinding2.widget.RxTextView
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonBoolean
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.response.User
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.services.LocationAddress
import com.np.namasteyoga.ui.trainer.searchtrainer.adapter.TrainerListAdapter
import com.np.namasteyoga.ui.trainer.trainerDetails.CustomDialogFragment
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.NetworkUtil
import com.np.namasteyoga.utils.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_trainer_search.*
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

class TrainerSearchListActivity : BaseActivity<TrainerSearchListViewModel>(
    TrainerSearchListViewModel::class
),
    OnClickItem<User>, PaginationListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener {
            finish()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val lat = LatLng(Util.citySelected?.lat?.toDouble()?:0.0, Util.citySelected?.lng?.toDouble()?:0.0)
        lstTrainerSearch.run {
            layoutManager = LinearLayoutManager(this@TrainerSearchListActivity)
            adapter = TrainerListAdapter(
                list,
                this@TrainerSearchListActivity,
                this@TrainerSearchListActivity,
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
                    Logger.Debug(location.toString(),TAG)
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
            list.clear()
            try {
//                toast(it.message ?: getString(R.string.something_went_wrong))
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    if (it.data?.isNotEmpty() == true) {
                        it.data.run {
                            list.addAll(this)
                        }
                    }

                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
            lstTrainerSearch.adapter?.notifyDataSetChanged()
        })

         RxTextView.textChanges(etSelectTrainer)
//            .skipInitialValue()
//            .debounce(400, TimeUnit.MILLISECONDS)
//            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Logger.Debug(msg = it.toString())
                if (city==null)
                    return@subscribe
                list.clear()
                if (it.isNotEmpty()) {
                    getUserList(it.toString().trim())
                }
                lstTrainerSearch.adapter?.notifyDataSetChanged()
            },{},{},{
                compositeDisposable.add(it)
            })



    }

    private var city: City? = null
    private fun getUserList(key:String) {
        city = Util.citySelected
        Logger.Debug(key)
        if (city == null) {
            toast(R.string.something_went_wrong)
            return
        }
        Logger.Debug("$TAG $key")
        if (NetworkUtil.isInternetAvailable(context)) {
            city?.run {
                model.getListWithPagination(this,key)
            }
        } else
            toast(R.string.no_internet_connection)
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
            Logger.Debug(locationAddress)
        }
    }

    override fun layout(): Int = R.layout.activity_trainer_search

    override fun tag(): String = TAG
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
    private var latLng:LatLng?=null
    override fun page() {
        if (model.response.value?.current_page == model.response.value?.last_page)
            return
    }

    companion object {
        private const val TAG = "TrainerSearchListActivity:::  "
    }

    override val title: String
        get() = CommonString.Empty
    override val isShowTitle: Boolean
        get() = CommonBoolean.FALSE
}