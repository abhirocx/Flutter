package com.np.namasteyoga.ui.myAccount

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.ui.updateAccount.center.CenterAccountUpdateActivity
import com.np.namasteyoga.ui.updateAccount.guest.GuestUserAccountUpdateActivity
import com.np.namasteyoga.ui.updateAccount.trainer.TrainerAccountUpdateActivity
import com.np.namasteyoga.utils.*
import com.np.namasteyoga.utils.UIUtils.getCapitalized
import kotlinx.android.synthetic.main.activity_my_account.*
import kotlinx.android.synthetic.main.button_colored_layout.*
import kotlinx.android.synthetic.main.toolbar_black_with_back_and_img.*
import org.jetbrains.anko.toast

class MyAccountActivity : BaseActivity<MyAccountViewModel>(MyAccountViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        settings.setImageResource(R.drawable.ic_edit_account)
        settings.setOnClickListener {
            model.userDetails.value?.roleId?.run {
                when(this){
                    CommonInt.Three->{
                     startActivityForResult(Intent(this@MyAccountActivity,
                         TrainerAccountUpdateActivity::class.java),100)
                    }
                    CommonInt.Two->{
                        startActivityForResult(Intent(this@MyAccountActivity,
                            CenterAccountUpdateActivity::class.java),100)
                    }
                    CommonInt.Five->{
                        startActivityForResult(Intent(this@MyAccountActivity,
                            GuestUserAccountUpdateActivity::class.java),100)
                    }
                }
            }
        }

        model.userDetails.observe(this, Observer {
            it?.run {
                tvName.text = (name ?: CommonString.NA).getCapitalized()
                tvDesignation.setText(UIUtils.getUserType(roleId))
//                etMobile.setData(phone ?: CommonString.NA, R.drawable.ic_phone)
                etMobile.setMyAccountData(getString(R.string.mobile_number),phone ?: CommonString.NA)
                etSittingCapacity.setMyAccountData(getString(R.string.sitting_capacity),(sitting_capacity?:0).toString())
                etName.setMyAccountData(getUserType(roleId),(name ?: CommonString.NA).getCapitalized())
//                etEmailId.setData(email ?: CommonString.NA, R.drawable.ic_email)
                etEmailId.setMyAccountData(getString(R.string.email_id),email ?: CommonString.NA)
//                etAddress.setData(address ?: CommonString.NA, R.drawable.ic_location_gray)
                etAddress.setMyAccountData(getString(R.string.address),address ?: CommonString.NA)
//                etCity.setData(cityName ?: CommonString.NA, R.drawable.ic_location_gray)
                etCity.setMyAccountData(getString(R.string.city),cityName ?: CommonString.NA)
                etTrainerType.setMyAccountData(getString(R.string.trainer_as),getTrainerType(userType ))
                if (roleId == 5){
                    etAddress.hide()
                    etCity.hide()
                }
                if (roleId == 3){
                    etTrainerType.show()
                }else{
                    etTrainerType.hide()
                }

                if (roleId == 2){
                    etSittingCapacity.show()
                }else{
                    etSittingCapacity.hide()
                }

            }
        })
        buttonColorLayoutTxt.setText(R.string.suspend_account)

        buttonColorLayout.setOnClickListener {
            suspendAccountPopup()
        }

        model.response.observe(this, Observer {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@Observer
            }
            try {
                val status = (it.status ?: C.NP_STATUS_FAIL)
                if (status == C.NP_INVALID_TOKEN || status == C.NP_TOKEN_EXPIRED || status == C.NP_TOKEN_NOT_FOUND) {
                    SharedPreferencesUtils.setUserDetails(model.sharedPreferences, null)
                    setResult(RESULT_OK)
                    finish()
                    return@Observer
                }

                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    SharedPreferencesUtils.setUserDetails(model.sharedPreferences, null)
                    setResult(RESULT_OK)
                    finish()
                } else {
                    toast(it.message ?: getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })
    }
    private fun getUserType(roleId:Int?):String{
        return when(roleId){
            CommonInt.Three->{
                getString(R.string.trainer_name)
            }
            CommonInt.Two->{
                getString(R.string.centre_name)
            }
            else->{
                getString(R.string.full_name)
            }
        }
    }
    private fun getTrainerType(roleId:String?):String{
        return when(roleId){
            "Govt"->{
                getString(R.string.government)
            }
            else->{
                roleId?:CommonString.NA
            }
        }
    }


    private fun suspendAccountPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.suspend_account_confirmation_msg)
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
                    suspendAccount()
                }
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
            }
        }

    private fun suspendAccount() {
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.suspendAccount()
        } else
            toast(R.string.no_internet_connection)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        model.getUserDetails()
    }
    companion object {
        private const val TAG = "MyAccountActivity ::: "
    }

    override fun layout(): Int = R.layout.activity_my_account

    override fun tag(): String = TAG

    override val title: String
        get() = getString(R.string.my_accounts)
    override val isShowTitle: Boolean
        get() = true
}