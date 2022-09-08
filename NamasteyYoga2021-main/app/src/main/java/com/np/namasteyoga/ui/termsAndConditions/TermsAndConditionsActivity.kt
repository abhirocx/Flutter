package com.np.namasteyoga.ui.termsAndConditions

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.utils.IntentUtils
import com.np.namasteyoga.utils.UIUtils
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*
import kotlinx.android.synthetic.main.toolbar_black_with_back.*

class TermsAndConditionsActivity : AppCompatActivity() {

    companion object{
        const val PRIVACY = "privacy.html"
        const val TERMS = "terms_and_conditions.html"
    }
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)
        UIUtils.setStatusBarGradient(this, R.color.white)
        webView.settings.run {
            domStorageEnabled = true
            databaseEnabled = true
            javaScriptEnabled = true
        }
        webView.run {
            webChromeClient = WebChromeClient()
//            loadUrl("file:///android_asset/terms/terms_and_conditions.html")
//            loadUrl("${BuildConfig.BASE_URL}terms_and_conditions.html")
            val url  = intent?.getStringExtra(IntentUtils.SHARE_STRING)?: TERMS
            loadUrl("${BuildConfig.BASE_URL}$url")
        }

        onBackPressedClick.setOnClickListener {
            finish()
        }
    }


}