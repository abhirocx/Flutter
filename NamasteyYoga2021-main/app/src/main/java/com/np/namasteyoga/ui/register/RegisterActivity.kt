package com.np.namasteyoga.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.datasource.pojo.RegisterRequest
import com.np.namasteyoga.ui.otpVerify.OTPVerifyActivity
import com.np.namasteyoga.ui.register.adapter.RegisterCollectionAdapter
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.IntentUtils
import com.np.namasteyoga.utils.NetworkUtil
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast


class RegisterActivity : BaseActivity<RegisterViewModel>(RegisterViewModel::class) {

    private val userTypes by lazy { resources.getStringArray(R.array.userTypes) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val demoCollectionAdapter = RegisterCollectionAdapter(this)
        pager.adapter = demoCollectionAdapter
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = userTypes[position]
            changeTitle(position)
        }.attach()


        changeTitle()
        model.response.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {

                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    startOTPVerifyActivity()
                } else {
                    toast(it.message ?: getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })

        pager.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                changeTitle(position)
            }

        })
    }

    private fun changeTitle(position: Int = CommonInt.Zero){
        detailRegister.text = getString(
            R.string.please_provide_all_required_details_to_register_as_a_s,
            userTypes[position]
        )

    }

    private fun startOTPVerifyActivity() {
        val intent = Intent(context, OTPVerifyActivity::class.java)
        intent.putExtra(IntentUtils.SHARE_USER_DETAILS, registerRequest)
        startActivityForResult(intent, CommonInt.Fourty)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                CommonInt.Fourty -> {
                    setResult(RESULT_OK)
                    finishAfterTransition()
                }
            }
        }
    }
    private var registerRequest:RegisterRequest?=null
    fun register(registerRequest: RegisterRequest) {
        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            this.registerRequest = registerRequest
            model.register(registerRequest)
        } else
            toast(R.string.no_internet_connection)
    }

    override fun layout(): Int = R.layout.activity_register

    override fun tag(): String = TAG

    companion object {
        private const val TAG = "RegisterActivity:::  "
    }

    override val title: String
        get() = getString(R.string.register_)
    override val isShowTitle: Boolean
        get() = false
}