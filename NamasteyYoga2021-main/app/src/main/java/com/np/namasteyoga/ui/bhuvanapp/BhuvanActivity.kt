package com.np.namasteyoga.ui.bhuvanapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.response.SocialLinkModel
import com.np.namasteyoga.ui.selectCity.SelectCityViewModel
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.show
import kotlinx.android.synthetic.main.activity_social_link.*
import org.jetbrains.anko.toast
import android.content.pm.PackageManager




class BhuvanActivity : BaseActivity<BhuvanAppViewModel>(BhuvanAppViewModel::class) {
    private val list by lazy { ArrayList<SocialLinkModel>() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.response.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    if (it.data?.isNotEmpty() == true) {
//                        it.data.run {
//                            list.addAll(this)
//                        }
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                    if (list.isNotEmpty())
                        recyclerView.show()

                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })

//        model.getListWithPagination()
    }

    companion object{
        private const val TAG = "BhuvanActivity:::  "
    }

    override fun layout(): Int = R.layout.activity_bhuvan

    override fun tag(): String = TAG

    override val title: String
        get() = "Worldwide Events"
    override val isShowTitle: Boolean
        get() = true


    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

}