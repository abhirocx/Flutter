package com.np.namasteyoga.ui.ayushMerchandise.categoryList

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.interfaces.PaginationListener
import org.jetbrains.anko.toast
import com.np.namasteyoga.datasource.response.AyushMerchandiseModel
import com.np.namasteyoga.interfaces.ClickItem
import com.np.namasteyoga.ui.ayushMerchandise.categoryList.adapter.AyushMerchandiseCatagoryAdapter
import com.np.namasteyoga.ui.ayushMerchandise.subcategory.AyushSubCategoryActivity
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_ayush_catagory.*


class AyushCatagoryActivity : BaseActivity<AyushCatagoryViewModel>(AyushCatagoryViewModel::class),
    ClickItem<AyushMerchandiseModel>, PaginationListener {

    private val list by lazy { ArrayList<AyushMerchandiseModel>() }
    private val adapterAsana by lazy { AyushMerchandiseCatagoryAdapter(list, this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView.run {
            layoutManager = GridLayoutManager(this@AyushCatagoryActivity, 2)
            adapter = adapterAsana
        }
        model.responseAyush.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
            try {
                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                    if (it.data?.isNotEmpty() == true) {
                        it.data.run {
                         list.addAll(this)
                        }
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                    if (list.isNotEmpty()) {
                        noRecordFound.hide()
                        recyclerView.show()
                    }

                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })
        getAyushCategoryList()
    }

    private fun getAyushCategoryList() {

        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.getAyushCategoryList()
        } else
            toast(R.string.no_internet_connection)
    }

    override fun layout(): Int = R.layout.activity_ayush_catagory

    override fun tag(): String = TAG


    companion object {
        private const val TAG = "AsanaCatagoryActivity :::: "
    }

    override val title: String
        get() = getString(R.string.ayush_merchandise)
    override val isShowTitle: Boolean
        get() = true



    override fun page() {
        if (model.responseAyush.value?.data?.isNotEmpty()==true) {
            getAyushCategoryList()
        }

    }

    override fun onClickPosition(position: Int, t: AyushMerchandiseModel?) {
        t?.run {
            val intent = Intent(this@AyushCatagoryActivity, AyushSubCategoryActivity::class.java)
            intent.putExtra(IntentUtils.SHARE_RESULT, t)
            startActivity(intent)
        }
    }
}



