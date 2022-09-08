package com.np.namasteyoga.ui.changeLangage

import android.os.Bundle
import com.np.namasteyoga.R
import com.np.namasteyoga.base.BaseActivity
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.np.namasteyoga.utils.UIUtils
import kotlinx.android.synthetic.main.activity_change_language.*
import org.jetbrains.anko.toast

class ChangeLanguageActivity : BaseActivity<ChangeLanguageViewModel>(ChangeLanguageViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UIUtils.setStatusBarGradient(this, R.color.white)

        setCheckButton(SharedPreferencesUtils.getLanguageString(model.sharedPreferences))


        hindi.setOnClickListener {
            setHindiLanguage()
        }
        english.setOnClickListener {
            setEnglishLanguage()
        }
    }

    private fun setHindiLanguage(){
        setLanguage(SharedPreferencesUtils.SecondLanguage)
        setCheckButton(SharedPreferencesUtils.SecondLanguage)
        SharedPreferencesUtils.setLanguageString(model.sharedPreferences,SharedPreferencesUtils.SecondLanguage)
    }
    private fun setEnglishLanguage(){
        setLanguage()
        setCheckButton()
        SharedPreferencesUtils.setLanguageString(model.sharedPreferences)
    }

    private fun setCheckButton(lan:String=SharedPreferencesUtils.defaultLanguage){
        when(lan){
            SharedPreferencesUtils.SecondLanguage->{
                hindi.setCompoundDrawablesWithIntrinsicBounds(CommonInt.Zero, CommonInt.Zero, R.drawable.ic_checked, CommonInt.Zero)
                english.setCompoundDrawablesWithIntrinsicBounds(CommonInt.Zero, CommonInt.Zero, CommonInt.Zero, CommonInt.Zero)
            }
            else->{
                hindi.setCompoundDrawablesWithIntrinsicBounds(CommonInt.Zero, CommonInt.Zero, CommonInt.Zero, CommonInt.Zero)
                english.setCompoundDrawablesWithIntrinsicBounds(CommonInt.Zero, CommonInt.Zero, R.drawable.ic_checked, CommonInt.Zero)
            }
        }
    }

    override fun layout(): Int = R.layout.activity_change_language

    override fun tag(): String = TAG

    companion object{
        private const val TAG = "ChangeLanguageActivity::  "
    }

    override val title: String
        get() = getString(R.string.change_language)
    override val isShowTitle: Boolean
        get() = true
}