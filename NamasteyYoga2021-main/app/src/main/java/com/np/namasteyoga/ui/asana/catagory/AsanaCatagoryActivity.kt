package com.np.namasteyoga.ui.asana.catagory

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.ui.asana.catagory.adapter.AsanaCatagoryAdapter
import com.np.namasteyoga.ui.asana.subcategory.AsanaSubCategoryActivity
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.NetworkUtil
import com.np.namasteyoga.utils.show
import kotlinx.android.synthetic.main.activity_asana_catagory.*
import org.jetbrains.anko.toast
import com.np.namasteyoga.datasource.response.AsanaCategoryModel
import com.np.namasteyoga.utils.hide


class AsanaCatagoryActivity : BaseActivity<AsanaCatagoryViewModel>(AsanaCatagoryViewModel::class),
    OnClickItem<AsanaCategoryModel>, PaginationListener {

    private val list by lazy { ArrayList<AsanaCategoryModel>() }
    private val adapterAsana by lazy { AsanaCatagoryAdapter(list, this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView.run {
            layoutManager = GridLayoutManager(this@AsanaCatagoryActivity, 2)
            adapter = adapterAsana
        }
        model.response.observe(this, {
            isShowDialog(false)
            if (it == null) {
                toast(R.string.something_went_wrong)
                return@observe
            }
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
                                videosSum =0
                            }

                            list.addAll(listFilter)
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
        getAsanaList()
    }

    private fun getAsanaList() {

        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.getAsanaList()
        } else
            toast(R.string.no_internet_connection)
    }

    override fun layout(): Int = R.layout.activity_asana_catagory

    override fun tag(): String = TAG


    companion object {
        private const val TAG = "AsanaCatagoryActivity :::: "
    }

    override val title: String
        get() = getString(R.string.yoga_asana)
    override val isShowTitle: Boolean
        get() = true

    override fun onClick(position: Int, t: AsanaCategoryModel?) {
        t?.run {
            var intent = Intent(this@AsanaCatagoryActivity, AsanaSubCategoryActivity::class.java)
            val gson = Gson()
            intent.putExtra("categories", gson.toJson(list.get(position)))
            startActivity(intent)
        }
    }

    override fun page() {
        if (C.NP_STATUS_SUCCESS == model.response.value?.status) {
            getAsanaList()
        }

    }
}



