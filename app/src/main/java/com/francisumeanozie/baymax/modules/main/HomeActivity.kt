package com.francisumeanozie.baymax.modules.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.francisumeanozie.baymax.R
import com.francisumeanozie.baymax.data.responses.CurrencyResponse
import com.francisumeanozie.baymax.data.responses.ExchangeResponse
import com.francisumeanozie.baymax.databinding.ActivityHomeBinding
import com.francisumeanozie.baymax.models.Quote
import com.francisumeanozie.baymax.modules.base.BaseActivity
import com.francisumeanozie.baymax.utils.adapters.RateAdapter
import com.francisumeanozie.baymax.utils.extensions.setupObserver
import com.francisumeanozie.baymax.utils.sealeds.RequestError
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: ActivityHomeBinding
    private val adapter: RateAdapter by lazy {
        RateAdapter()
    }


    private var currencyList = arrayListOf<String>()
    private var quoteList = listOf<Quote>()
    private var quotes = arrayListOf<Quote>()
    private var index: Int = 0
    private var amountEntered: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)

        viewModel= ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        setupObservers()
        setUpViews()
    }

    private fun setUpViews(){
        rateRecyclerView.adapter = adapter
        rateRecyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun setupObservers() = with(viewModel) {
        viewModel.fetchCurrencies()
        viewModel.currencyIdItemPosition.observe(this@HomeActivity, {
            index = it
            //Log.e("currency", spinnerCur.selectedItem.toString())
        })

        //viewModel.fetchQuotes("USD")
        viewModel.amount.observe(this@HomeActivity, {
            if(it == "" || it == null){
                rateRecyclerView.visibility = View.GONE
                empty.visibility = View.VISIBLE
            }else{
                amountEntered = it.replace(",", "").toInt()
                rateRecyclerView.visibility = View.VISIBLE
                empty.visibility = View.GONE
                getQuotes()
            }

        })

        setupObserver(fetchQuoteSuccessLiveData to ::fetchQuoteObserver)
        setupObserver(quoteFailureLiveData to ::onQuoteFailure)
        setupObserver(fetchCurrencySuccessLiveData to ::fetchCurrencyObserver)
        setupObserver(currencyFailureLiveData to ::onCurrencyFailure)
        setupObserver(loadingLiveData to ::loadingObserver)
    }

    private fun loadingObserver(isLoading: Boolean) {
        loading.isVisible = isLoading
    }

    private fun fetchCurrencyObserver(response: CurrencyResponse){

        currencyList.add(0, "Select Currency")

        if(response.currencies != null){
            response.currencies.forEach{ (key, _) ->
                currencyList.add(key)
            }

            spinnerCur.adapter =
                this.let { ArrayAdapter(it, R.layout.spinner_item, currencyList) }
            (spinnerCur.adapter as ArrayAdapter<*>?)?.notifyDataSetChanged()
        }

    }

    private fun fetchQuoteObserver(response: ExchangeResponse){

        if(response.quotes != null){
            response.quotes.forEach{ (key, value) ->

                val newQuote = Quote(key, amountEntered * value)
                //Log.e("Quote", key)
                quotes.add(newQuote)

            }

            quoteList = quotes
            adapter.updateQuote(quoteList)
        }

    }

    private fun onCurrencyFailure(error: RequestError) {
        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
        this.run {
            //errorDialog.show(this.supportFragmentManager, PaystackBankFragment.TAG_BANK_DIALOG)
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onQuoteFailure(error: RequestError) {

        Toast.makeText(this, "something went wrong on getting quotes", Toast.LENGTH_SHORT).show()
        this.run {
            //errorDialog.show(this.supportFragmentManager, PaystackBankFragment.TAG_BANK_DIALOG)
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getQuotes(){
        when  {
            (spinnerCur.selectedItem.toString() == "") -> {
                (spinnerCur.selectedView as TextView).error = "Required"
                return
            }
            (spinnerCur.selectedItem.toString() == "Select Currency") -> {
                (spinnerCur.selectedView as TextView).error = "Required"
                return
            }
            (spinnerCur.selectedItem.toString() != "Select Currency") -> {
                viewModel.fetchQuotes(spinnerCur.selectedItem.toString())
            }
        }

    }

}