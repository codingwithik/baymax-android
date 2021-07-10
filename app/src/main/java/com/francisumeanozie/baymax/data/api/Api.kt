package com.francisumeanozie.baymax.data.api

import com.francisumeanozie.baymax.data.responses.CurrencyResponse
import com.francisumeanozie.baymax.data.responses.ExchangeResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("list")
    fun getCurrencies(
        @Query("access_key") accessKey: String,
        @Query("format") format: Int
    ): Single<CurrencyResponse>

    @GET("live")
    fun getQuotes(
        @Query("access_key") accessKey: String,
        @Query("currencies") currencies: String,
        @Query("source") source: String,
        @Query("format") format: Int
    ): Single<ExchangeResponse>
}