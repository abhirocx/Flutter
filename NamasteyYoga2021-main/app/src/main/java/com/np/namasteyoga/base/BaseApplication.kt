package com.np.namasteyoga.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.ktx.initialize
import com.np.namasteyoga.modules.NetworkModule
import com.np.namasteyoga.modules.SharedPreference
import com.np.namasteyoga.modules.ViewModelModules
import com.np.namasteyoga.utils.ConstUtility
import com.np.namasteyoga.utils.RuntimeLocaleChanger
import com.np.namasteyoga.utils.Util
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import io.reactivex.plugins.RxJavaPlugins
import java.util.*


class BaseApplication : MultiDexApplication() {

    override fun onCreate() {

        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Firebase.initialize(this)
        startKoin {
            androidContext(this@BaseApplication)
            logger(AndroidLogger())
            modules(listOf(ViewModelModules.rxModule,
                SharedPreference.preferencesModule,
                ViewModelModules.viewModels,
                NetworkModule.networkModule
            ))
            RxJavaPlugins.setErrorHandler {}
        }
        if (!BuildConfig.DEBUG)
        try {
//            ConstUtility.handleSSLHandshake()
            registerActivityLifeCycle()
        }catch (e:Exception){}

    }
    private fun registerActivityLifeCycle(){
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks{
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                p0.window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
            }

            override fun onActivityStarted(p0: Activity) {

            }

            override fun onActivityResumed(p0: Activity) {

            }

            override fun onActivityPaused(p0: Activity) {

            }

            override fun onActivityStopped(p0: Activity) {

            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

            }

            override fun onActivityDestroyed(p0: Activity) {

            }

        })
    }

    private val localizationDelegate = LocalizationApplicationDelegate()

    override fun attachBaseContext(base: Context?) {
        if (base == null)return
        localizationDelegate.setDefaultLanguage(base, Locale.ENGLISH)
        super.attachBaseContext(localizationDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
//        localizationDelegate.onConfigurationChanged(this)
        RuntimeLocaleChanger.overrideLocale(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(baseContext, super.getResources())
    }


}