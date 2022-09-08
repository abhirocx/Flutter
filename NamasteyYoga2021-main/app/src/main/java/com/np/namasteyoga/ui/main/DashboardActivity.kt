package com.np.namasteyoga.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import com.np.namasteyoga.datasource.response.AyushMerchandiseModel
import com.np.namasteyoga.datasource.response.CelebrityTestimonialModel
import com.np.namasteyoga.interfaces.ClickItem
import com.np.namasteyoga.interfaces.ClickItem2
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.services.LocationAddress
import com.np.namasteyoga.ui.asana.catagory.AsanaCatagoryActivity
import com.np.namasteyoga.ui.asana.subcategory.AsanaSubCategoryActivity
import com.np.namasteyoga.ui.ayushMerchandise.categoryList.AyushCatagoryActivity
import com.np.namasteyoga.ui.ayushMerchandise.subcategory.AyushSubCategoryActivity
import com.np.namasteyoga.ui.bhuvanapp.BhuvanActivity
import com.np.namasteyoga.ui.celebrityTestimonial.CelebrityTestimonialActivity
import com.np.namasteyoga.ui.center.centerList.CenterListActivity
import com.np.namasteyoga.ui.eventModule.eventVideo.EventVideoActivity
import com.np.namasteyoga.ui.eventModule.fragments.FragmentEvents
import com.np.namasteyoga.ui.eventModule.fragments.FragmentUserEvents
import com.np.namasteyoga.ui.feedback.FeedbackActivity
import com.np.namasteyoga.ui.health.main.FitActionRequestCode
import com.np.namasteyoga.ui.health.main.HeathTrackerActivity
import com.np.namasteyoga.ui.login.LoginActivity
import com.np.namasteyoga.ui.main.adapter.AsanaCatagoryAdapter
import com.np.namasteyoga.ui.main.adapter.AyushMerchandiseCatagoryAdapter
import com.np.namasteyoga.ui.main.adapter.DashCelebrityTestimonialListAdapter
import com.np.namasteyoga.ui.myAccount.MyAccountActivity
import com.np.namasteyoga.ui.poll.PollListActivity
import com.np.namasteyoga.ui.quiz.QuizListActivity
import com.np.namasteyoga.ui.selectCity.SelectCityActivity
import com.np.namasteyoga.ui.settings.SettingsActivity
import com.np.namasteyoga.ui.social.SocialLinkActivity
import com.np.namasteyoga.ui.termsAndConditions.TermsAndConditionsActivity
import com.np.namasteyoga.ui.trainer.trainerList.TrainerListActivity
import com.np.namasteyoga.utils.*
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnClickItem<AsanaCategoryModel>, ClickItem<AyushMerchandiseModel>,
    ClickItem2<CelebrityTestimonialModel>, OnLocaleChangedListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val model: MainViewModel by viewModel(MainViewModel::class)


    private val list by lazy { ArrayList<AsanaCategoryModel>() }
    private val adapterAsana by lazy { AsanaCatagoryAdapter(list, this) }

    private val listAyush by lazy { ArrayList<AyushMerchandiseModel>() }
    private val adapterAyush by lazy { AyushMerchandiseCatagoryAdapter(listAyush, this) }

    private val listCelebrityTestimonial by lazy { ArrayList<CelebrityTestimonialModel>() }
    private val adapterCelebrityTestimonial by lazy { DashCelebrityTestimonialListAdapter(listCelebrityTestimonial, this) }


    private val localizationDelegate by lazy {   LocalizationActivityDelegate(this) }

    private fun startTermsAndConditionsActivity() {
        val intent = Intent(this, TermsAndConditionsActivity::class.java)
        intent.putExtra(IntentUtils.SHARE_STRING, TermsAndConditionsActivity.PRIVACY)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        localizationDelegate.addOnLocaleChangedListener(this)
        localizationDelegate.onCreate()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        UIUtils.setStatusBarGradientDash(this, R.color.white)

        setSupportActionBar(toolbar)
//        UIUtils.setStatusBarGradient(this, R.color.white)

        supportActionBar?.run {
            title = getString(R.string.welcome)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.black)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
//        changeMenuList()

        selectCity.setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, SelectCityActivity::class.java),
                102
            )
        }
//        toolbar.title = CommonString.Empty


        model.userDetails.observe(this, Observer {
            if (it != null) {
                it.run {
                    setHeaderText(R.id.txtUserType, getString(UIUtils.getUserType(roleId)))
                    getHeaderView(R.id.txtUserType).disable()
                    setHeaderText(
                        R.id.txtUserName,
                        (name ?: getString(R.string.app_name)).getCapitalized()
                    )
                }
            } else {
                setHeaderText(R.id.txtUserType, getString(R.string.login))
                getHeaderView(R.id.txtUserType).enable()
                setHeaderText(R.id.txtUserName, getString(R.string.app_name))
            }
            changeMenuList()
        })
        getHeaderView(R.id.txtUserType).setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, LoginActivity::class.java),
                100
            )
        }

        checkPermissionsAndRun(FitActionRequestCode.SUBSCRIBE)

        health.setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, HeathTrackerActivity::class.java),
                100
            )
        }
        trainers.setOnClickListener {
            if (Util.citySelected == null) {
                toast(getString(R.string.select_city_p))
                return@setOnClickListener
            }
            startActivityForResult(
                Intent(applicationContext, TrainerListActivity::class.java),
                100
            )
        }
        centers.setOnClickListener {
            if (Util.citySelected == null) {
                toast(getString(R.string.select_city_p))
                return@setOnClickListener
            }
            startActivityForResult(
                Intent(applicationContext, CenterListActivity::class.java),
                100
            )
        }
        events.setOnClickListener {
            if (Util.citySelected == null) {
                toast(getString(R.string.select_city_p))
                return@setOnClickListener
            }
            startActivityForResult(
//                Intent(applicationContext, EventActivity::class.java),
                Intent(applicationContext, FragmentEvents::class.java),
                100
            )
        }


        yogaAsanaRecyclerView.run {
            layoutManager =
                LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterAsana
        }
        merchandiseRecyclerView.run {
            layoutManager =
                LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterAyush
        }

        celebrityRecyclerView.run {
            layoutManager =
                LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterCelebrityTestimonial
        }

        tvViewAllAsana.setOnClickListener {
            startActivityForResult(Intent(this, AsanaCatagoryActivity::class.java),100)
        }
        tvViewAllMerchandise.setOnClickListener {
            startActivityForResult(Intent(this, AyushCatagoryActivity::class.java),100)
        }
        tvViewAllCelebrity.setOnClickListener {
            startActivityForResult(Intent(this, CelebrityTestimonialActivity::class.java),100)
        }

        getAsanaList()
        getAyushList()
        getCelebrityTestimonialList()
        model.responseAsana.observe(this, {
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            list.clear()
            try {
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    if (it.data?.isNotEmpty() == true) {
                        it.data.run {
                            var listFilter = arrayListOf<AsanaCategoryModel>()
                            var videosSum = 0
                            for (i in 0..(this.size?.minus(1)!!)) {

                                this.get(i).sub_category_data?.forEach {
                                    videosSum += it?.total_aasana

                                }


                                if (videosSum > 0) {
                                    listFilter.add(this.get(i))

                                }
                                videosSum = 0
                            }

                            list.addAll(listFilter)
                        }
                    }
                    yogaAsanaRecyclerView.adapter?.notifyDataSetChanged()


                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
            hideAndShowAsana()
        })
        model.responseAyush.observe(this, {
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            listAyush.clear()
            try {
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    if (it.data?.isNotEmpty() == true) {
                        it.data.run {
                            listAyush.addAll(this)
                        }
                    }
                    merchandiseRecyclerView.adapter?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
            hideAndShowAyush()
        })
        model.responseCelebrityTestimonial.observe(this, {
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            listCelebrityTestimonial.clear()
            try {
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    if (it.data?.isNotEmpty() == true) {
                        it.data.run {
                            listCelebrityTestimonial.addAll(this)
                        }
                    }
                    celebrityRecyclerView.adapter?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
            hideAndShowCelebrityTestimonial()
        })

        socialLayout.setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, SocialLinkActivity::class.java),
                100
            )
        }
        buvanAppLayout.setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, BhuvanActivity::class.java),
                100
            )
        }

        pollLayout.setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, PollListActivity::class.java),
                100
            )
        }
        quizLayout.setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, QuizListActivity::class.java),
                100
            )
        }
        feedbackLayout.setOnClickListener {
            if (model.userDetails.value == null) {
                startActivityForResult(
                    Intent(applicationContext, LoginActivity::class.java),
                    100
                )
                return@setOnClickListener
            }
            startActivityForResult(
                Intent(applicationContext, FeedbackActivity::class.java),
                100
            )
        }

    }

//    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // There are no request codes
//            val data: Intent? = result.data
//            doSomeOperations()
//        }
//    }

    private fun setHeaderText(@IdRes id: Int, msg: String) {
        nav_view.getHeaderView(CommonInt.Zero)?.find<TextView>(id)?.text = msg
    }

    private fun getHeaderView(@IdRes id: Int): View {
        return nav_view.getHeaderView(CommonInt.Zero).find(id)
    }

    private fun changeMenuList() {
        nav_view.menu.clear()
        val id =
            if (model.sharedPreferences.run(SharedPreferencesUtils::getUserDetails) != null) {
                R.menu.activity_main_drawer
            } else {
                R.menu.main_drawer
            }
        nav_view.inflateMenu(id)
    }

    override fun onResume() {
        super.onResume()
        localizationDelegate.onResume(this)
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() = if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
        drawer_layout.closeDrawer(GravityCompat.START)
    } else {
//        backPressedHandler()
        ActivityCompat.finishAffinity(this)
    }

    private var backCount = CommonInt.Zero
    private fun backPressedHandler() {
        if (backCount == CommonInt.Zero) {
            showSnackBar()
        }
        backCount++
        if (backCount > 1) {
            ActivityCompat.finishAffinity(this)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            backCount = CommonInt.Zero
        }, CommonInt.KB.toLong())
    }

    private fun showSnackBar() {
        Snackbar.make(
            drawer_layout,
            getString(R.string.back_press_msg),
            CommonInt.KB * CommonInt.Two
        )
            .show()
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

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

    private fun checkPermissionsAndRun(fitActionRequestCode: FitActionRequestCode) {
        if (permissionApproved()) {

            if (ConstUtility.isGpsEnabled(this)) {
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
            val alertBuilder = AlertDialog.Builder(this)
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

    companion object {
        private const val TAG = "DashboardActivity::   "
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when {
            grantResults.isEmpty() -> {
                Logger.Debug(tag = TAG, msg = "User interaction was cancelled.")
            }
            grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                if (ConstUtility.isGpsEnabled(this)) {
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
                Logger.Debug("==================================== $this")
                Util.citySelected = this
                selectCity.text = Util.citySelected?.city
            }
            Logger.Debug(locationAddress)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.myAccount -> {
                startActivityForResult(
                    Intent(applicationContext, MyAccountActivity::class.java),
                    103
                )
            }
            R.id.settings -> {
                startActivityForResult(
                    Intent(applicationContext, SettingsActivity::class.java),
                    100
                )
            }
//            R.id.login -> {
//                startActivityForResult(
//                    Intent(applicationContext, LoginActivity::class.java),
//                    100
//                )
//            }
            R.id.center -> {
                if (Util.citySelected == null) {
                    toast(getString(R.string.select_city_p))
                    return true
                }
                startActivityForResult(
                    Intent(applicationContext, CenterListActivity::class.java),
                    100
                )
            }
            R.id.myEvent -> {
//                if (Util.citySelected == null) {
//                    toast(getString(R.string.select_city_p))
//                    return true
//                }
                startActivityForResult(
                    Intent(applicationContext, FragmentUserEvents::class.java),
                    100
                )
            }
            R.id.trainer -> {
                if (Util.citySelected == null) {
                    toast(getString(R.string.select_city_p))
                    return true
                }
                startActivityForResult(
                    Intent(applicationContext, TrainerListActivity::class.java),
                    100
                )
            }
            R.id.health -> {
                startActivityForResult(
                    Intent(applicationContext, HeathTrackerActivity::class.java),
                    100
                )
            }

            R.id.quiz -> {
                startActivityForResult(
                    Intent(applicationContext, QuizListActivity::class.java),
                    100
                )
            }

            R.id.poll -> {
                startActivityForResult(
                    Intent(applicationContext, PollListActivity::class.java),
                    100
                )
            }
            R.id.feedback -> {
                if (model.userDetails.value == null) {
                    startActivityForResult(
                        Intent(applicationContext, LoginActivity::class.java),
                        100
                    )
                    return true
                }
                startActivityForResult(
                    Intent(applicationContext, FeedbackActivity::class.java),
                    100
                )
            }
            R.id.social -> {
                startActivityForResult(
                    Intent(applicationContext, SocialLinkActivity::class.java),
                    100
                )
            }
            R.id.asana -> {
                startActivityForResult(
                    Intent(applicationContext, AsanaCatagoryActivity::class.java),
                    106
                )
                //startActivity(Intent(this, AsanaCatagoryActivity::class.java))

            }
//            R.id.ayush -> {
//                startActivity(Intent(this, AyushCatagoryActivity::class.java))
//            }
            R.id.celebrity->{
                startActivityForResult(Intent(this, CelebrityTestimonialActivity::class.java),100)
            }
            R.id.share->{
                shareTextToOtherApps()
            }
            R.id.privacy->{
                if (NetworkUtil.isInternetAvailable(this))
                    startTermsAndConditionsActivity()
                else{
                    toast(R.string.no_internet_connection)
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun shareTextToOtherApps() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_msg))
            type = getString(R.string.text_type)
        }

        val shareIntent = Intent.createChooser(sendIntent, getString(R.string.share_app_title))
        startActivity(shareIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
//                CommonInt.hundred,103 -> {
//                    model.getUserDetails()
//                }
                102 -> {
                    if (data != null && data.hasExtra(C.CITY)) {
                        Util.citySelected = data.getSerializableExtra(C.CITY) as City?
                        selectCity.text = Util.citySelected?.city
                    }
                }
                1010 -> {
                    checkPermissionsAndRun(FitActionRequestCode.SUBSCRIBE)
                }

            }

        }
        model.getUserDetails()
    }

    private fun getAsanaList() {
        if (NetworkUtil.isInternetAvailable(this)) {
            model.getAsanaList()
        } else
            toast(R.string.no_internet_connection)
    }

    private fun getAyushList() {
        if (NetworkUtil.isInternetAvailable(this)) {
            model.getAyushCategoryList()
        } else
            toast(R.string.no_internet_connection)
    }
    private fun getCelebrityTestimonialList() {
        if (NetworkUtil.isInternetAvailable(this)) {
            model.getCelebrityTestimonialList()
        } else
            toast(R.string.no_internet_connection)
    }

    private fun hideAndShowAsana() {
        if (list.isNullOrEmpty()) {
            yogaAsanaHeader.hide()
            yogaAsanaRecyclerView.hide()
            asanaLayout.hide()
        } else {
            yogaAsanaHeader.show()
            yogaAsanaRecyclerView.show()
            asanaLayout.show()
        }
    }

    private fun hideAndShowAyush() {
        if (listAyush.isNullOrEmpty()) {
            merchandiseHeader.hide()
            merchandiseRecyclerView.hide()
            ayushLayout.hide()
        } else {
            merchandiseHeader.show()
            merchandiseRecyclerView.show()
            ayushLayout.show()
        }
    }

    private fun hideAndShowCelebrityTestimonial() {
        if (listCelebrityTestimonial.isNullOrEmpty()) {
            celebrityHeader.hide()
            celebrityRecyclerView.hide()
            celebrityLayout.hide()
        } else {
            celebrityHeader.show()
            celebrityRecyclerView.show()
            celebrityLayout.show()
        }
    }

    override fun onClick(position: Int, t: AsanaCategoryModel?) {
        t?.run {
            val intent = Intent(this@DashboardActivity, AsanaSubCategoryActivity::class.java)
            val gson = Gson()
            intent.putExtra("categories", gson.toJson(list[position]))
            startActivity(intent)
        }
    }

    override fun onClickPosition(position: Int, t: AyushMerchandiseModel?) {
        t?.run {
            val intent = Intent(this@DashboardActivity, AyushSubCategoryActivity::class.java)
            intent.putExtra(IntentUtils.SHARE_RESULT, t)
            startActivity(intent)
        }
    }

    override fun onClickPositionNo(position: Int, t: CelebrityTestimonialModel?) {
        t?.run {
            if (NetworkUtil.isInternetAvailable(this@DashboardActivity)) {
                if (vedio_id.isNullOrEmpty()) {
                    toast(R.string.something_went_wrong)
                } else {
                    val intent = Intent(this@DashboardActivity, EventVideoActivity::class.java)
                    intent.putExtra(IntentUtils.SHARE_STRING, vedio_id)
                    startActivity(intent)

                }
            } else {
                toast(R.string.no_internet_connection)
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase))
        super.attachBaseContext(newBase)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(super.getResources())
    }

    fun setLanguage(language: String?) {
        localizationDelegate.setLanguage(this, language!!)
    }

    fun setLanguage(locale: Locale?) {
        localizationDelegate.setLanguage(this, locale!!)
    }

    val currentLanguage: Locale
        get() = localizationDelegate.getLanguage(this)

    // Just override method locale change event
    override fun onBeforeLocaleChanged() {}
    override fun onAfterLocaleChanged() {}


}