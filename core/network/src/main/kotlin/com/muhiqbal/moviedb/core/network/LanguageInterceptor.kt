package com.muhiqbal.moviedb.core.network

import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import javax.inject.Inject

class LanguageInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val url = original.url.newBuilder()
            .setQueryParameter("language", currentLanguageTag())
            .build()
        return chain.proceed(original.newBuilder().url(url).build())
    }

    private fun currentLanguageTag(): String {
        val locale = Locale.getDefault()
        val language = when (locale.language) {
            "in" -> "id"
            else -> locale.language.ifBlank { "en" }
        }
        val region = locale.country
        return if (region.isNotBlank()) "$language-$region" else language
    }
}
