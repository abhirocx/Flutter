package com.np.namasteyoga.ui.celebrityTestimonial

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.interfaces.PaginationListener
import org.jetbrains.anko.toast
import com.np.namasteyoga.datasource.response.CelebrityTestimonialModel
import com.np.namasteyoga.interfaces.ClickItem2
import com.np.namasteyoga.ui.celebrityTestimonial.adapter.CelebrityTestimonialListAdapter
import com.np.namasteyoga.ui.eventModule.eventVideo.EventVideoActivity
import com.np.namasteyoga.utils.*
import kotlinx.android.synthetic.main.activity_celebrity_testimonial.*


class CelebrityTestimonialActivity : BaseActivity<CelebrityTestimonialViewModel>(CelebrityTestimonialViewModel::class),
    ClickItem2<CelebrityTestimonialModel>, PaginationListener {

    private val list by lazy { ArrayList<CelebrityTestimonialModel>() }
    private val adapterCelebrityTestimonial by lazy { CelebrityTestimonialListAdapter(list, this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView.run {
            layoutManager = GridLayoutManager(this@CelebrityTestimonialActivity, 2)
            adapter = adapterCelebrityTestimonial
        }
        model.responseCelebrityTestimonial.observe(this, {
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
        getCelebrityTestimonialList()
    }

    private fun getCelebrityTestimonialList() {

        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.getCelebrityTestimonialList()
        } else
            toast(R.string.no_internet_connection)
    }

    override fun layout(): Int = R.layout.activity_celebrity_testimonial

    override fun tag(): String = TAG


    companion object {
        private const val TAG = "CelebrityTestimonialActivity :::: "
    }

    override val title: String
        get() = getString(R.string.celebrity_testimonial)
    override val isShowTitle: Boolean
        get() = true



    override fun page() {
        if (model.responseCelebrityTestimonial.value?.data?.isNotEmpty()==true) {
            getCelebrityTestimonialList()
        }

    }


    override fun onClickPositionNo(position: Int, t: CelebrityTestimonialModel?) {
        t?.run {
            if (NetworkUtil.isInternetAvailable(context)) {
                if (vedio_id.isNullOrEmpty()) {
                    toast(R.string.something_went_wrong)
                } else {
                    val intent = Intent(context, EventVideoActivity::class.java)
                    intent.putExtra(IntentUtils.SHARE_STRING, vedio_id)
                    startActivity(intent)

                }
            } else {
                toast(R.string.no_internet_connection)
            }
        }
    }
}



