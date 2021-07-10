package com.francisumeanozie.baymax.utils.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*

inline fun <reified T : ViewModel> AppCompatActivity.getViewModel() = ViewModelProviders.of(this).get(T::class.java)

fun <T> AppCompatActivity.setupObserver(
    pair: Pair<LiveData<T>, (T) -> Unit>
) = pair.first.observe(this, Observer { it?.let(pair.second) })

fun AppCompatActivity.setupSingleEventObserver(
    pair: Pair<MutableLiveData<Unit>, () -> Unit>
) = pair.first.observe(this, Observer { pair.second() })