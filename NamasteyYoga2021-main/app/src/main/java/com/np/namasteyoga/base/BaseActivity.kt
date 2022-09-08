package com.np.namasteyoga.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import com.google.firebase.FirebaseApp
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.interfaces.LayoutImplement
import com.np.namasteyoga.ui.center.centerList.CenterListActivity
import com.np.namasteyoga.ui.center.searchCenters.CenterSearchListActivity
import com.np.namasteyoga.ui.changePassword.ChangePasswordActivity
import com.np.namasteyoga.ui.fcmTestActivity.TestFcmActivity
import com.np.namasteyoga.ui.forgotPassword.ForgotPasswordActivity
import com.np.namasteyoga.ui.health.main.HeathTrackerActivity
import com.np.namasteyoga.ui.login.LoginActivity
import com.np.namasteyoga.ui.main.DashboardActivity
import com.np.namasteyoga.ui.main.MainActivity
import com.np.namasteyoga.ui.register.RegisterActivity
import com.np.namasteyoga.ui.trainer.searchtrainer.TrainerSearchListActivity
import com.np.namasteyoga.ui.trainer.trainerList.TrainerListActivity
import com.np.namasteyoga.utils.Constants.currentYogaType
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.toolbar_with_back_and_img.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.reflect.KClass


abstract class BaseActivity<T : ViewModel>(clazz: KClass<T>) : AppCompatActivity(),
    LayoutImplement, OnLocaleChangedListener {


    private val TAG: String by lazy { tag() }

    val model: T by viewModel(clazz)

    val context: Context by lazy { this }
    private val progressDialog by lazy { AlertDialog.Builder(this) }
    private var dialog: Dialog? = null
    protected val compositeDisposable by lazy { CompositeDisposable() }

    private val localizationDelegate by lazy {   LocalizationActivityDelegate(this)}

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        localizationDelegate.addOnLocaleChangedListener(this)
        localizationDelegate.onCreate()
        super.onCreate(savedInstanceState)
        Logger.Debug(getString(R.string.onCreate), TAG)
        FirebaseApp.initializeApp(context)
        setContentView(layout())



        progressDialog.setView(R.layout.progress)
        progressDialog.setCancelable(false)
        dialog = progressDialog.create()

        onBackPressedClick?.setOnClickListener {
            finish()
        }
        if (isShowTitle) {
            toolbarTitle?.text = title
        }
    }


    protected fun startHealthActivity() {
        startActivity(Intent(context, HeathTrackerActivity::class.java))

    }
    protected fun startTestFCMActivity() {
        startActivity(Intent(context, TestFcmActivity::class.java))

    }

    protected fun startRegisterActivity() {
        startActivityForResult(Intent(context, RegisterActivity::class.java), CommonInt.hundred)


    }

    protected fun startLoginActivity() {
        startActivityForResult(Intent(context, LoginActivity::class.java), CommonInt.hundredOne)
    }
    protected fun startDashboardActivity() {
        startActivityForResult(Intent(context, DashboardActivity::class.java), CommonInt.DASHBOARD)
    }

    protected fun startChangePasswordActivity() {
        startActivityForResult(
            Intent(context, ChangePasswordActivity::class.java),
            CommonInt.ChangePassword
        )
    }

    protected fun startForgotPasswordActivity() {
        startActivity(Intent(context, ForgotPasswordActivity::class.java))
    }

    protected fun startTrainerListActivity() {
        startActivity(Intent(context, TrainerListActivity::class.java))
    }
    protected fun startCenterListActivity() {
        startActivity(Intent(context, CenterListActivity::class.java))
    }

    protected fun startSearchTrainerListActivity() {
        currentYogaType = "trainer"
        startActivity(Intent(context, TrainerSearchListActivity::class.java))
    }
    protected fun startSearchCenterListActivity() {
        startActivity(Intent(context, CenterSearchListActivity::class.java))
    }

    protected fun startMainActivity() {
        startActivity(Intent(context, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        hideKeyboard()
        Logger.Debug(getString(R.string.onBackPressed), TAG)
        super.onBackPressed()
    }

    protected fun hideKeyboard() {
        val view = this.currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, CommonInt.Zero)
        }
    }

    protected fun showKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, CommonInt.Zero)
    }


    protected fun isShowDialog(isShow: Boolean, isKey: Boolean = true) {
        if (isShow) {
            if (isKey)
                hideKeyboard()
            dialog?.show()
        } else
            dialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.dismiss()
        compositeDisposable.clear()
    }

    protected fun showToast(stringId: String?, isFinishActivity: Boolean) {
        try {
            val factory = LayoutInflater.from(this)
            val deleteDialogView = factory.inflate(
                R.layout.toast_layout, null
            )
            val deleteDialog = android.app.AlertDialog.Builder(this).create()
            deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            deleteDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            deleteDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            deleteDialog.setView(deleteDialogView)
            val textView = deleteDialogView.findViewById<View>(R.id.tvToast) as TextView
            textView.text = stringId
            deleteDialog.show()
            deleteDialog.setCancelable(false)
            val timer2 = Timer()
            timer2.schedule(object : TimerTask() {
                override fun run() {
                    deleteDialog.dismiss()
                    timer2.cancel()
                    if (isFinishActivity) finish()
                }
            }, 2000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    public override fun onResume() {
        super.onResume()
        localizationDelegate.onResume(this)
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

    fun setLanguage(language: String=SharedPreferencesUtils.defaultLanguage) {
        localizationDelegate.setLanguage(this, language)
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