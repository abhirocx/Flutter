package com.np.namasteyoga.ui.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.ui.changeLangage.ChangeLanguageActivity
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.toast

class SettingsActivity : BaseActivity<SettingsViewModel>(SettingsViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UIUtils.setStatusBarGradient(this, R.color.white)


        model.tokenUpdate()
        model.response.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {

                toast(it.message ?: getString(R.string.something_went_wrong))
                val status = (it.status ?: C.NP_STATUS_FAIL)
                if (status == C.NP_INVALID_TOKEN || status == C.NP_TOKEN_EXPIRED || status == C.NP_TOKEN_NOT_FOUND) {
                    googleSignOut()
                    SharedPreferencesUtils.setUserDetails(model.sharedPreferences, null)
                    setResult(RESULT_OK)
                    finish()
                    return@observe
                }
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS || (it.status
                        ?: C.NP_STATUS_FAIL) == CommonString.One
                ) {
                    googleSignOut()
                    SharedPreferencesUtils.setUserDetails(model.sharedPreferences, null)
                    SharedPreferencesUtils.setPushTokenOnOff(model.sharedPreferences,1)
                    setResult(RESULT_OK)
                    isLogout()
                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })

        pushNotification.setOnClickListener {
            updateFCMToken()
        }
        changeLanguage.setOnClickListener {
            openSomeActivityForResult()
        }
        changePassword.setOnClickListener {
            startChangePasswordActivity()
        }
        logout.setOnClickListener {
            logoutPopup()
        }

        if (model.token.value == null) {
            isLogout()
        }
        model.responseR.observe(this, {
            isShowDialog(false)
            toast(it?.message ?: getString(R.string.something_went_wrong))
            pushNotification.isChecked =
                SharedPreferencesUtils.getPushTokenOnOff(model.sharedPreferences) != CommonInt.Zero
        })
        pushNotification.isChecked =
            SharedPreferencesUtils.getPushTokenOnOff(model.sharedPreferences) != CommonInt.Zero


    }
    private fun openSomeActivityForResult() {
        val intent = Intent(this, ChangeLanguageActivity::class.java)
//        resultLauncher.launch(intent)
        startActivityForResult(intent,100)
    }
//    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // There are no request codes
//            val data: Intent? = result.data
//            model.getUserDetails()
//        }
//    }

    private fun googleSignOut() {
//        val fitnessOptions = FitnessOptions.builder()
//            .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
//            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
//            .addDataType(DataType.TYPE_DISTANCE_DELTA)
//            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
//            .build()
//        Fitness.getConfigClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
//            .disableFit()
//            .addOnSuccessListener {
//                Log.i(TAG, "Disabled Google Fit")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "There was an error disabling Google Fit", e)
//            }
        GoogleSignIn.getClient(applicationContext, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
    }


    private fun updateFCMToken() {
        UIUtils.setToken(model.sharedPreferences)

        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.updateFCMToken()
        } else
            toast(R.string.no_internet_connection)
    }

    private fun logout() {
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.logout()
        } else
            toast(R.string.no_internet_connection)
    }

    private fun isLogout() {
        logoutLayout.hide()
//        pushNotificationLayout.hide()
        changePasswordLayout.hide()
    }

    private fun logoutPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.logout_msg)
            .setPositiveButton(R.string.yes, dialogClickListener)
            .setNegativeButton(R.string.no, dialogClickListener)
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
    }

    private val dialogClickListener =

        DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    logout()
                }
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
            }
        }


    override fun layout(): Int = R.layout.activity_settings

    override fun tag(): String = TAG

    companion object {
        private const val TAG = "SettingsActivity ::: "
    }

    override val title: String
        get() = getString(R.string.settings)
    override val isShowTitle: Boolean
        get() = true
}