package com.francisumeanozie.baymax.utils.extensions

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.defaultSubscription(actionAfterTerminate: (() -> Unit)? = { Unit }): Single<T> {
    return this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doAfterTerminate(actionAfterTerminate)
}

fun Completable.defaultSubscription(actionAfterTerminate: (() -> Unit)? = { Unit }): Completable {
    return this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doAfterTerminate(actionAfterTerminate)
}