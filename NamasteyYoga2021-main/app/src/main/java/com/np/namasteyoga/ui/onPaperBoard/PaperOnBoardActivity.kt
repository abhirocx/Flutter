package com.np.namasteyoga.ui.onPaperBoard

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonBoolean
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.ui.onPaperBoard.adapter.OnPaperModel
import com.np.namasteyoga.ui.onPaperBoard.adapter.ScreenSlidePagerAdapter
import com.np.namasteyoga.ui.onPaperBoard.fragments.ViewPagerContentFragment
import com.np.namasteyoga.utils.Logger
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.ramotion.paperonboarding.PaperOnboardingPage
import kotlinx.android.synthetic.main.activity_paper_on_board_update.*

class PaperOnBoardActivity : BaseActivity<OnPaperBoardViewModel>(OnPaperBoardViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        val onBoardingFragment = PaperOnboardingFragment.newInstance(getDataForOnBoarding())
//
//
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.add(R.id.fragment_container, onBoardingFragment)
//        fragmentTransaction.commitAllowingStateLoss()

        fragment_container.adapter = ScreenSlidePagerAdapter(this,getDataForOnPaperBoarding())
        circleIndicator.setViewPager(fragment_container)
        fragment_container.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                val id = if (position > CommonInt.Two) {
                    R.string.finish
                } else {
                    R.string.skip
                }
                skip.setText(id)
                super.onPageSelected(position)
            }
        })

        skip.setOnClickListener {
            SharedPreferencesUtils.setIsFirstTimeOnPaper(
                model.sharedPreferences,
                CommonBoolean.TRUE
            )
            startDashboardActivity()
            finish()
        }

    }

    private val imgList =
        listOf(R.drawable.art1, R.drawable.art2, R.drawable.art3, R.drawable.art4)

    private fun getDataForOnBoarding(): ArrayList<PaperOnboardingPage> {
        val titles = resources.getStringArray(R.array.OnPagerBoardTitles)
        val msgs = resources.getStringArray(R.array.OnPagerBoardMsgs)
        val list = ArrayList<PaperOnboardingPage>()
        val color = Color.parseColor("#FFFFFF")
        for (i in 0..3) {
            val model = PaperOnboardingPage(
                titles[i],
                msgs[i],
                color,
                imgList[i],
                R.drawable.dot_selected
            )
            list.add(model)
        }
        return list
    }

    private fun getDataForOnPaperBoarding(): ArrayList<Fragment> {
        val titles = resources.getStringArray(R.array.OnPagerBoardTitles)
        val msgs = resources.getStringArray(R.array.OnPagerBoardMsgs)
        val list = ArrayList<Fragment>()
        for (i in 0..3) {
            list.add(
                ViewPagerContentFragment.newInstance(
                    OnPaperModel(
                        imgList[i],
                        titles[i],
                        msgs[i]
                    )
                )
            )
        }
        return list
    }

    //    override fun layout(): Int = R.layout.activity_paper_on_board
    override fun layout(): Int = R.layout.activity_paper_on_board_update

    override fun tag(): String = TAG

    companion object {
        private const val TAG = "PaperOnBoardActivity"
    }

    override val title: String
        get() = CommonString.Empty
    override val isShowTitle: Boolean
        get() = CommonBoolean.FALSE
}