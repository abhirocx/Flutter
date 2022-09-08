package com.np.namasteyoga.ui.social

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.datasource.response.SocialLinkModel
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.ui.social.adapter.SocialLinkAdapter
import com.np.namasteyoga.utils.C
import com.np.namasteyoga.utils.NetworkUtil
import com.np.namasteyoga.utils.show
import kotlinx.android.synthetic.main.activity_social_link.*
import org.jetbrains.anko.toast

class SocialLinkActivity : BaseActivity<SocialViewModel>(SocialViewModel::class) ,
    OnClickItem<SocialLinkModel>, PaginationListener {
    private val list by lazy { ArrayList<SocialLinkModel>() }
    private val socialLinkAdapter by lazy { SocialLinkAdapter(list, this, this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@SocialLinkActivity)
            adapter = socialLinkAdapter
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
                            list.addAll(this)
                        }
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
        getSocialLinks()

    }
    private fun getSocialLinks() {

        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.getSocialLinks()
        } else
            toast(R.string.no_internet_connection)
    }
    override fun layout(): Int = R.layout.activity_social_link

    override fun tag(): String = TAG

    companion object{
        private const val TAG = "SocialLinkActivity::: "
    }

    override val title: String
        get() = getString(R.string.social_media)
    override val isShowTitle: Boolean
        get() = true

    override fun onClick(position: Int, t: SocialLinkModel?) {

    }

    override fun page() {
        if (C.NP_STATUS_SUCCESS == model.response.value?.status) {
            getSocialLinks()
        }
    }
}