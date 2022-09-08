package com.np.namasteyoga.ui.ayushMerchandise.subcategory

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.response.AyushMerchandiseModel
import com.np.namasteyoga.datasource.response.ayushSubCategoryModels.AyushSubCategoryList
import com.np.namasteyoga.interfaces.ClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.ui.ayushMerchandise.detailScreen.AyushProductDetailActivity
import com.np.namasteyoga.ui.ayushMerchandise.subcategory.adapter.AyushMerchandiseSubCatagoryAdapter
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_ayush_sub_category.*
import kotlinx.android.synthetic.main.toolbar_black_with_back.*
import org.jetbrains.anko.toast

class AyushSubCategoryActivity : BaseActivity<AyushSubCatagoryViewModel>(AyushSubCatagoryViewModel::class),
    ClickItem<AyushSubCategoryList>, PaginationListener {

    private var ayushMerchandiseModel: AyushMerchandiseModel?=null

    private val list by lazy { ArrayList<AyushSubCategoryList>() }
    private val adapterAsana by lazy { AyushMerchandiseSubCatagoryAdapter(list, this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        recyclerView.run {
            layoutManager = LinearLayoutManager(this@AyushSubCategoryActivity)
            adapter = adapterAsana
        }

        intent?.run {
            if (hasExtra(IntentUtils.SHARE_RESULT)){
                ayushMerchandiseModel = getParcelableExtra(IntentUtils.SHARE_RESULT)

                ayushMerchandiseModel?.run {
                    getAyushCategoryList()
                    toolbarTitle.text= category_name?:CommonString.Empty
                }
            }
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
                        recyclerView.show()
                        noRecordFound.hide()
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
            model.getAyushSubCategoryList(ayushMerchandiseModel?.id?.toString()?:CommonString.Empty)
        } else
            toast(R.string.no_internet_connection)
    }


    override fun layout(): Int =R.layout.activity_ayush_sub_category

    override fun tag(): String = TAG

    companion object{
        private const val TAG = "AyushSubCategoryActivity :: "
    }

    override val title: String
        get() = CommonString.Empty
    override val isShowTitle: Boolean
        get() = true

    override fun onClickPosition(position: Int, t: AyushSubCategoryList?) {
        t?.run {
            val intent = Intent(this@AyushSubCategoryActivity, AyushProductDetailActivity::class.java)
            intent.putExtra(IntentUtils.SHARE_RESULT_SERVICE, t)
            startActivity(intent)
        }
    }

    override fun page() {
        if (model.responseAyush.value?.data?.isNotEmpty()==true) {
            getAyushCategoryList()
        }
    }
}