package com.francisumeanozie.baymax.utils.repositories

import com.francisumeanozie.baymax.BuildConfig
import com.francisumeanozie.baymax.data.api.Api
import com.francisumeanozie.baymax.data.responses.CurrencyResponse
import com.francisumeanozie.baymax.data.responses.ExchangeResponse
import com.francisumeanozie.baymax.utils.helpers.QuoteRetrofitHelper
import com.francisumeanozie.baymax.utils.helpers.RetrofitHelper
import io.reactivex.Single
import java.util.*

class CurrencyRepository {

    private val currencyApi = RetrofitHelper.createClient().create(Api::class.java)
    private val quoteApi = QuoteRetrofitHelper.createClient().create(Api::class.java)

    fun getCurrencies(): Single<CurrencyResponse> {
        return currencyApi.getCurrencies(BuildConfig.ACCESS_KEY, 1)
    }

    fun getQuotes(currency: String): Single<ExchangeResponse> {
        return quoteApi.getQuotes(BuildConfig.ACCESS_KEY, "", "", 1)
    }

}