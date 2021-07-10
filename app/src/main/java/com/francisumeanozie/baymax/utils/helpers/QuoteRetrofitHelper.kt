package com.francisumeanozie.baymax.utils.helpers

import android.util.Log
import com.francisumeanozie.baymax.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object QuoteRetrofitHelper {

    fun createClient(): Retrofit {

        val builder = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .callTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)


        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor { message -> Log.d("HttpLoggingInterceptor", message) }
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addNetworkInterceptor(httpLoggingInterceptor)
        }

        val client = builder.build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_QUOTE_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}