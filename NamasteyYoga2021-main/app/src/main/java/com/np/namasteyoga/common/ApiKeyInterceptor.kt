package com.np.namasteyoga.common

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("deviceplatform", "android")
                    //cache control
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Cache-Control", "no-store")
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0")
                .build()
        )
    }
}