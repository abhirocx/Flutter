package com.np.namasteyoga.ui.ayushMerchandise.detailScreen

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.response.ayushSubCategoryModels.AyushSubCategoryList
import com.np.namasteyoga.datasource.response.ayushSubCategoryModels.ProductImage
import com.np.namasteyoga.ui.ayushMerchandise.detailScreen.adapter.ProductDetailScreenSlidePagerAdapter
import com.np.namasteyoga.utils.*
import com.np.namasteyoga.utils.UIUtils.getSplit
import kotlinx.android.synthetic.main.activity_ayush_product_detail.*
import kotlinx.android.synthetic.main.activity_ayush_product_detail.circleIndicator
import kotlinx.android.synthetic.main.activity_ayush_product_detail.fragment_container
import kotlinx.android.synthetic.main.toolbar_green_with_cross.*
import org.jetbrains.anko.toast

class AyushProductDetailActivity : BaseActivity<AyushProductDetailViewModel>(
    AyushProductDetailViewModel::class
) {

    private var ayushMerchandiseModel: AyushSubCategoryList?=null

    private val list =  ArrayList<ProductImage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UIUtils.setStatusBarGradient(this,R.color.colorPrimaryDark)

        fragment_container.adapter = ProductDetailScreenSlidePagerAdapter(this,list)

        intent?.run {
            if (hasExtra(IntentUtils.SHARE_RESULT_SERVICE)){
                ayushMerchandiseModel = getParcelableExtra(IntentUtils.SHARE_RESULT_SERVICE)
                ayushMerchandiseModel?.run {
                    setData(this)
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
                    it.data?.run(::setData)
                } else {
                    toast(it.message ?: getString(R.string.something_went_wrong))
                }
            } catch (e: Exception) {
                toast(R.string.something_went_wrong)
                if (C.DEBUG)
                    e.printStackTrace()
            }
        })
        getAyushCategoryList()

    }
    private fun setData(ayushSubCategoryList: AyushSubCategoryList){
        ayushSubCategoryList.run {
            toolbarTitle.text= product_name?:CommonString.Empty
            tvOverview.run {
                addShowMoreText(getString(R.string.read_more))
                setShowingLine(4)
                setShowMoreColor(ContextCompat.getColor(this@AyushProductDetailActivity, R.color.colorPrimary))
                text = product_description ?: CommonString.Empty
            }

            val cap = (key_ingredients?:CommonString.Empty).getSplit()
            Logger.Debug(cap)
            val key = " ■ " + cap.replace(
                CommonString.ATtheRate.toRegex(),
                "<br/> ■ "
            )
            tvKeyIngredients.text= HtmlCompat.fromHtml(key, HtmlCompat.FROM_HTML_MODE_LEGACY)
            tvDirection.text= direction?:CommonString.Empty

            list.clear()
            if(images?.isNotEmpty()==true) {
                list.addAll(images)
            }
            if(product_image?.isNotEmpty()==true) {
                list.addAll(product_image)
            }
            fragment_container.adapter?.notifyDataSetChanged()
            circleIndicator.setViewPager(fragment_container)
        }
    }
    private fun getAyushCategoryList() {

        if (NetworkUtil.isInternetAvailable(context)) {
            isShowDialog(true)
            model.getAyushProductDetails(
                ayushMerchandiseModel?.id?.toString() ?: CommonString.Empty
            )
        } else
            toast(R.string.no_internet_connection)
    }

    override fun layout(): Int = R.layout.activity_ayush_product_detail

    override fun tag(): String = TAG

    companion object{
        private const val TAG = "AyushProductDetailActivity :: ="
    }

    override val title: String
        get() = CommonString.Empty
    override val isShowTitle: Boolean
        get() = true
}