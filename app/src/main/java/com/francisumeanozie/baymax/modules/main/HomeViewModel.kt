package com.francisumeanozie.baymax.modules.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.francisumeanozie.baymax.data.responses.CurrencyResponse
import com.francisumeanozie.baymax.data.responses.ExchangeResponse
import com.francisumeanozie.baymax.models.Quote
import com.francisumeanozie.baymax.modules.base.BaseViewModel
import com.francisumeanozie.baymax.utils.extensions.defaultSubscription
import com.francisumeanozie.baymax.utils.helpers.SingleLiveEvent
import com.francisumeanozie.baymax.utils.repositories.CurrencyRepository
import com.francisumeanozie.baymax.utils.sealeds.RequestError
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel : BaseViewModel() {

    override val compositeDisposable = CompositeDisposable()
    override val loading = MutableLiveData<Boolean>()

    val currencyIdItemPosition = MutableLiveData<Int>()

    val amount = MutableLiveData<String>()

    private val quotes = MutableLiveData<List<Quote>>()
    val quoteLiveData: LiveData<List<Quote>>
        get() = quotes

    private val fetchCurrencySuccess = SingleLiveEvent<CurrencyResponse>()
    val fetchCurrencySuccessLiveData: SingleLiveEvent<CurrencyResponse>
        get() = fetchCurrencySuccess

    private val currencyFailure = MutableLiveData<RequestError>()
    val currencyFailureLiveData: LiveData<RequestError>
        get() = currencyFailure

    private val fetchQuoteSuccess = SingleLiveEvent<ExchangeResponse>()
    val fetchQuoteSuccessLiveData: SingleLiveEvent<ExchangeResponse>
        get() = fetchQuoteSuccess

    private val quoteFailure = MutableLiveData<RequestError>()
    val quoteFailureLiveData: LiveData<RequestError>
        get() = quoteFailure

    private val isLoading = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean>
        get() = isLoading

    private val currencyRepository by lazy {
        CurrencyRepository()
    }

    fun fetchCurrencies() {
        isLoading.postValue(true)

        val disposable = currencyRepository.getCurrencies()
            .defaultSubscription {
                isLoading.postValue(false)
            }
            .subscribe({
                onFetchCurrencySuccess(it)
            }, {
                currencyFailure(it)
            })

        compositeDisposable.add(disposable)
    }

    fun fetchQuotes(currency: String) {
        val disposable = currencyRepository.getQuotes(currency)
            .doOnSubscribe { showLoading() }
            .defaultSubscription { hideLoading() }
            .subscribe({
                onFetchQuoteSuccess(it)
            }, {
                onFetchQuoteFailure(it)
            })
        compositeDisposable.add(disposable)
    }


    private fun onFetchCurrencySuccess(response: CurrencyResponse) {
        fetchCurrencySuccess.value = response
    }

    private fun currencyFailure(throwable: Throwable) {
        currencyFailure.value = handleRequestError(throwable)
    }

    private fun onFetchQuoteSuccess(response: ExchangeResponse) {
        fetchQuoteSuccess.value = response
    }

    private fun onFetchQuoteFailure(throwable: Throwable) {
        quoteFailure.value = handleRequestError(throwable)
    }

}