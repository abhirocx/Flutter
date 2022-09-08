package com.np.namasteyoga.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

object SharedPreference {



    const val DataPrefs = "dataPrefs"
    const val SecurePrefs = "securePrefs"

    val preferencesModule = module {

        single(named(DataPrefs)) { provideSettingsPreferences(androidApplication()) }
        single(named(SecurePrefs)) { provideSecurePreferences(androidApplication()) }
    }


    private fun provideSettingsPreferences(app: Application): SharedPreferences =
        app.getSharedPreferences(DataPrefs, Context.MODE_PRIVATE)

    private fun provideSecurePreferences(app: Application): SharedPreferences =
        app.getSharedPreferences(SecurePrefs, Context.MODE_PRIVATE)


}