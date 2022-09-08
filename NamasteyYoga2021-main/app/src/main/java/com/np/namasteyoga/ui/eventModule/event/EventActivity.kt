package com.np.namasteyoga.ui.eventModule.event

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.ui.eventModule.adapter.EventCollectionAdapter
import com.np.namasteyoga.utils.hide
import com.np.namasteyoga.utils.setColoredButtonText
import com.np.namasteyoga.utils.show
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.button_colored_layout_without_round.*
import org.jetbrains.anko.find


class EventActivity : BaseActivity<EventViewModel>(EventViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buttonColorLayoutTxt.setText(R.string.register_login)
        val demoCollectionAdapter = EventCollectionAdapter(this)
        viewPager.run {
            adapter = demoCollectionAdapter
            currentItem = 1
            offscreenPageLimit = 3

        }

        btnCurrent.setOnClickListener {
            viewPager.setCurrentItem(0, true)
            it.setColorUpdate()
        }
        btnUpcoming.setOnClickListener {
            viewPager.setCurrentItem(1, true)
            it.setColorUpdate()
        }
        btnPast.setOnClickListener {
            viewPager.setCurrentItem(2, true)
            it.setColorUpdate()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0->{
                        btnCurrent.setColorUpdate()
                    }
                    1->{
                        btnUpcoming.setColorUpdate()
                    }
                    2->{
                        btnPast.setColorUpdate()
                    }
                }
            }
        })

        btnUpcoming.setColorUpdate()
        btnUpcoming.setColoredButtonText(getString(R.string.upcoming_events))
        btnPast.setColoredButtonText(getString(R.string.past_events))
        btnCurrent.setColoredButtonText(getString(R.string.current_events))
        model.token.observe(this, Observer {
            if (it != null)
                buttonColorLayout.hide()
            else
                buttonColorLayout.show()
        })

    }
    private fun View.setColorUpdate(){
        btnCurrent.changeButtonColor()
        btnUpcoming.changeButtonColor()
        btnPast.changeButtonColor()
        changeButtonColor(true)
    }

    private fun View.changeButtonColor(isColored:Boolean=false){

        val text = find<TextView>(R.id.buttonColorLayoutTxt)
        text.background?.run {
           val draw =  DrawableCompat.wrap(this)
           val back = if (isColored){
                R.color.colorPrimaryDark
            }else{
                R.color.light_gray_
            }
            val textColor = if (isColored){
                R.color.white
            }else{
                R.color.text_black_
            }
            DrawableCompat.setTint(draw, ContextCompat.getColor(context,back))
            text.background  = draw
            text.setTextColor(ContextCompat.getColor(context,textColor))
        }

    }

    override fun layout(): Int = R.layout.activity_event

    override fun tag(): String = TAG

    companion object{
        private const val TAG = "EventActivity:: "
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        model.getIsLogin()
    }

    override val title: String
        get() = getString(R.string.yoga_events)
    override val isShowTitle: Boolean
        get() = true
}