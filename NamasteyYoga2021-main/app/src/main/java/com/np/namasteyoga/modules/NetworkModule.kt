package com.np.namasteyoga.modules


import android.content.Context
import android.os.Build
import com.google.gson.GsonBuilder
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.common.ApiKeyInterceptor
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.retofit.RetrofitHandShake
import com.np.namasteyoga.retofit.mode.builder.PinBuilder
import com.np.namasteyoga.retofit.pin_extractor.PinExtract
import com.np.namasteyoga.retofit.pin_extractor.PinExtractor
import com.np.namasteyoga.utils.C
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


object NetworkModule {

    const val WaitTimeOut: Long = 30
    const val ConnTimeOut: Long = 10
    val networkModule = module {

        single(named(OKHTTP)) { provideDefaultOkhttpClient() }
        single(named(RETROFIT)) {
            provideRetrofit(
                get(named(OKHTTP)),
                androidContext().applicationContext
            )
        }
        single(named(RETROFIT_API)) { provideTmdbService(get(named(RETROFIT))) }

        single(named(OK_HTTP_GOOGLE)) { provideDefaultOkhttpClientGoogle() }
        single(named(RETROFIT_GOOGLE)) { provideRetrofitGoogle(get(named(OK_HTTP_GOOGLE))) }
        single(named(RETROFIT_API_GOOGLE)) { provideTmdbServiceGoogle(get(named(RETROFIT_GOOGLE))) }

    }

    fun provideDefaultOkhttpClient(): OkHttpClient {

        return if (C.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.HEADERS
            logging.level = HttpLoggingInterceptor.Level.BODY

            OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor())
                .addNetworkInterceptor(logging)
                .callTimeout(WaitTimeOut, TimeUnit.SECONDS)
                .connectTimeout(ConnTimeOut, TimeUnit.SECONDS)
                .readTimeout(WaitTimeOut, TimeUnit.SECONDS)
                .followRedirects(false)
                .hostnameVerifier(HostnameVerifier { hostname, _ ->
                    BuildConfig.BASE_URL.contains(
                        hostname
                    )
                })
                .followSslRedirects(false)
                .retryOnConnectionFailure(true)
                .cache(null)
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor())
                .callTimeout(WaitTimeOut, TimeUnit.SECONDS)
                .connectTimeout(ConnTimeOut, TimeUnit.SECONDS)
                .readTimeout(WaitTimeOut, TimeUnit.SECONDS)
                .followRedirects(false)
                .hostnameVerifier(HostnameVerifier { hostname, _ ->
                    BuildConfig.BASE_URL.contains(
                        hostname
                    )
                })
                .followSslRedirects(false)
                .retryOnConnectionFailure(true)
                .cache(null)
                .build()
        }

    }

    fun provideDefaultOkhttpClientGoogle(): OkHttpClient {


        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .callTimeout(WaitTimeOut, TimeUnit.SECONDS)
            .connectTimeout(ConnTimeOut, TimeUnit.SECONDS)
            .readTimeout(WaitTimeOut, TimeUnit.SECONDS)
            .build()

    }


    fun gson() = GsonBuilder()
        .create()

    fun provideRetrofit(client: OkHttpClient, application: Context): Retrofit {

        val extractPin: PinExtract? = PinExtractor.newBuilder(application, "sha256")
            .open(BuildConfig.CRT_FILE)
            .build()

        val handShakeMode = PinBuilder.newBuilder(BuildConfig.BASE_URL)
            .pinKey(extractPin)
            .build()

        val client1 = RetrofitHandShake
            .mode(handShakeMode)
            .handshake(client)

        val client = OkHttpClient()


        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun provideRetrofitGoogle(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(C.GOOGLE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    fun provideTmdbService(retrofit: Retrofit): RESTApi = retrofit.create(RESTApi::class.java)
    fun provideTmdbServiceGoogle(retrofit: Retrofit): RESTApi = retrofit.create(RESTApi::class.java)

    private const val OKHTTP = "okHttp"
    const val RETROFIT = "retrofit"
    const val RETROFIT_API = "retrofit_api"

    private const val OK_HTTP_GOOGLE = "ok_http_google"
    const val RETROFIT_GOOGLE = "retrofit_google"
    const val RETROFIT_API_GOOGLE = "retrofit_api_google"

    const val RETROFIT_C = "retrofit_c"
    const val RETROFIT_API_C = "retrofit_api_c"


}