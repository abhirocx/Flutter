package com.np.namasteyoga.ui.splash

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonBoolean
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.interfaces.ConfirmationListener
import com.np.namasteyoga.ui.health.main.FitActionRequestCode
import com.np.namasteyoga.ui.login.LoginActivity
import com.np.namasteyoga.ui.onPaperBoard.PaperOnBoardActivity
import com.np.namasteyoga.utils.*
import com.np.namasteyoga.utils.NetworkUtil.showNetworkPopup
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class SplashActivity : BaseActivity<SplashViewModel>(SplashViewModel::class) ,ConfirmationListener{

    private val getStartBottomSheet by lazy { GetStartBottomSheet(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        setContentView(R.layout.activity_splash)
//        window.setFlags(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.MATCH_PARENT
//        )


//        AnimationUtils.loadAnimation(this, R.anim.fade_in).reset()

//        getVersionApi()

//        val myRoundedBottomSheet = MyRoundedBottomSheet()
//        myRoundedBottomSheet.show(supportFragmentManager, myRoundedBottomSheet.tag)

        if (RootUtil.isDeviceRooted(this)){
            Toast.makeText(this,getString(R.string.root_msg), Toast.LENGTH_LONG).show()
            finish()
            return
        }
        model.response.observe(this, {

            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    val currentVersion = Version(ConstUtility.decrypt(it.data?.version))
                   // val appVersion = Version(C.VERSION_NAME)

//                    if (currentVersion > appVersion) {
//                        showPopUpToUpdateApp()
//                    }
//                    else {
//                        if (!SharedPreference.getInstance(context).getBoolean(C.IS_FIRST_LAUNCH)) {
//                            SharedPreference.getInstance(context).setBoolean(C.IS_FIRST_LAUNCH, CommonBoolean.TRUE)
//                            disclaimerPopup()
//                        } else {
//                            startMainActivity()
//                        }
//                    }

                    if (!SharedPreference.getInstance(context).getBoolean(C.IS_FIRST_LAUNCH)) {
                        SharedPreference.getInstance(context)
                            .setBoolean(C.IS_FIRST_LAUNCH, CommonBoolean.TRUE)
                        disclaimerPopup()
                    } else {
//                        startMainActivity()
//                        startHealthActivity()
                        startPaperOnBoardActivity()
                    }
                } else {
                    it.message?.run(::toast)
                }
            } catch (e: Exception) {
                showPopUpToUpdateApp()
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })

//        chart.run {
//            val list = ArrayList<GraphChartModel>()
//            list.add(GraphChartModel(GraphChartUtils.getRandomColor(),89))
//            list.add(GraphChartModel(GraphChartUtils.getRandomColor(),189))
//            list.add(GraphChartModel(GraphChartUtils.getRandomColor(),289))
//            list.add(GraphChartModel(GraphChartUtils.getRandomColor(),389))
//            list.add(GraphChartModel(GraphChartUtils.getRandomColor(),89))
//            list.add(GraphChartModel(GraphChartUtils.getRandomColor(),409))
//            list.add(GraphChartModel(GraphChartUtils.getRandomColor(),119))
//            setBarMaxValue(500)
//            addBar(list)
//        }

//        startHealthActivity()


//        if (SharedPreferencesUtils.getIsFirstTimeOnPaper(model.sharedPreferences)) {
//            checkPermissionsAndRun(FitActionRequestCode.SUBSCRIBE)
//        }else{
//            getStartBottomSheet.isCancelable = false
//            Handler(Looper.getMainLooper()).postDelayed({
//                getStartBottomSheet.show(supportFragmentManager,GetStartBottomSheet.TAG)
//            },500)
//
//        }
        getStartBottomSheet.isCancelable = false
        Handler(Looper.getMainLooper()).postDelayed({
            getStartBottomSheet.show(supportFragmentManager,GetStartBottomSheet.TAG)
        },500)

        updateFCMToken()

    }

    private fun requestPermissionForStorage() {
        try {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) &&
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) &&
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                ) &&
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) &&
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                getDialogConfirm(
                    getString(R.string.permission_msg),
                    getString(R.string.permission_title)
                )
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), CommonInt.One
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        try {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        && (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        && (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                        && (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        && (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            } else { //permission is automatically granted on sdk<23 upon installation
                true
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }


    private fun getDialogConfirm(dataText: String?, titleText: String?) {
        try {
            val dialog = Dialog(context)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_with_two_button)
            dialog.setCancelable(CommonBoolean.FALSE)
            val dataTextTv = dialog.find<TextView>(R.id.dialog_data_text)
            val titleTextTv = dialog.find<TextView>(R.id.dialog_title_text)
            val cancelTv = dialog.find<TextView>(R.id.dialog_cancel_text)
            val okTextTv = dialog.find<TextView>(R.id.dialog_ok_text)
            cancelTv.hide()
            dataTextTv.text = dataText
            titleTextTv.text = titleText
            cancelTv.setOnClickListener { dialog.dismiss() }
            okTextTv.setOnClickListener {
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + C.APPLICATION_ID)
                    )
                )
                dialog.dismiss()
            }
            dialog.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    private fun showPopUpToUpdateApp() {
        val alertBuilder =
            AlertDialog.Builder(context)
        alertBuilder.setCancelable(false)
        alertBuilder.setTitle(R.string.update_required)
        alertBuilder.setMessage(R.string.it_is_mandatory_to_update_the_app)
        alertBuilder.setPositiveButton(
            R.string.update
        ) { dialog, _ ->
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + C.APPLICATION_ID)))
            startActivity(Intent(context, LoginActivity::class.java))
            dialog.dismiss()
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    private fun disclaimerPopup() {
        val alertBuilder = AlertDialog.Builder(context, R.style.MyAppTheme)
        alertBuilder.setCancelable(false)
        alertBuilder.setTitle(R.string.disclaimer)
        alertBuilder.setMessage(R.string.disclaimer_text)
        alertBuilder.setPositiveButton(
            R.string.iagree
        ) { dialog, _ ->
            SharedPreference.getInstance(context).setBoolean(C.IS_FIRST_LAUNCH, true)
            dialog.dismiss()
//            startMainActivity()
            startPaperOnBoardActivity()
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private val runnable = Runnable {
        if (isStoragePermissionGranted()) {
            if (ConstUtility.isGpsEnabled(context)) {
                getVersionApi()
            } else {
                ConstUtility.showSettingsAlert(this)
            }
        } else {
            requestPermissionForStorage()
        }
    }

    private fun getVersionApi() {
        if (ConstUtility.isNetworkConnectivity(context)) {
            model.getVersion()
        } else {
            showNetworkPopup(context)
        }
    }

//    override fun onResume() {
//        super.onResume()
////        handler.postDelayed(runnable, C.SPLASH_LOADER_TIME)
//    }

    override fun onPause() {
        super.onPause()
//        handler.removeCallbacks(runnable)
    }

    private fun checkPermissionsAndRun(fitActionRequestCode: FitActionRequestCode) {
        if (permissionApproved()) {

            if (ConstUtility.isGpsEnabled(context)) {
                getVersionApi()
            } else {
                ConstUtility.showSettingsAlert(this)
            }
//            getVersionApi()
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
        UIUtils.setToken(model.sharedPreferences)
        updateFCMToken()
        when {
            grantResults.isEmpty() -> {
                Logger.Debug(tag = TAG, msg = "User interaction was cancelled.")
            }
            grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                if (ConstUtility.isGpsEnabled(context)) {
                    getVersionApi()
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
                // Request permission. It's possible this can be auto answered if device policy
                // sets the permission in a given state or the user denied the permission
                // previously and checked "Never ask again".
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
        UIUtils.setToken(model.sharedPreferences)
    }

    private fun startPaperOnBoardActivity() {
//        startActivity(Intent(context, HeathTrackerActivity::class.java))
        if (SharedPreferencesUtils.getIsFirstTimeOnPaper(model.sharedPreferences)) {
            startDashboardActivity()
        } else
            startActivity(Intent(context, PaperOnBoardActivity::class.java))
//        startActivity(Intent(context, TestFcmActivity::class.java))
        finish()
    }


    private fun updateFCMToken() {
        UIUtils.setToken(model.sharedPreferences)

        if (NetworkUtil.isInternetAvailable(context)) {
//            isShowDialog(true)
            model.updateFCMToken()
        } else
            toast(R.string.no_internet_connection)
    }

    companion object {
        private const val TAG = "SplashActivity"
    }

    override fun layout(): Int = R.layout.activity_splash

    override fun tag(): String = TAG

    override val title: String
        get() = TAG

    override val isShowTitle: Boolean
        get() = false

    override fun clickYes(isYes: Boolean) {
        checkPermissionsAndRun(FitActionRequestCode.SUBSCRIBE)
    }

}