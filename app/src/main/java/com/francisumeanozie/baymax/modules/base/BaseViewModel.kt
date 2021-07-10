package com.francisumeanozie.baymax.modules.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.francisumeanozie.baymax.BuildConfig
import com.francisumeanozie.baymax.utils.sealeds.RequestError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.UnknownHostException

abstract class BaseViewModel : ViewModel() {

    abstract val compositeDisposable: CompositeDisposable

    protected abstract val loading: MutableLiveData<Boolean>
    val loadingLiveData: LiveData<Boolean>
        get() = loading

    fun showLoading() = loading.postValue(true)

    fun hideLoading() = loading.postValue(false)

    private fun getHttpExceptionMessage(errorBody: ResponseBody): String? {
        val type = object : TypeToken<HashMap<String, String>>() {}.type
        return try {
            val errorContent: Map<String, String> = Gson().fromJson(errorBody.string(), type)
            errorContent.getValue(errorContent.keys.first())
        } catch (ex: Exception) {
            null
        }
    }

    fun handleRequestError(throwable: Throwable): RequestError {
        if (BuildConfig.DEBUG)
            throwable.printStackTrace()

        return when (throwable) {
            is UnknownHostException -> RequestError.NetworkConnection
            is HttpException -> {
                val errorBody = throwable.response()?.errorBody()
                val message = if (errorBody != null) {
                    getHttpExceptionMessage(errorBody)
                } else {
                    null
                }

                RequestError.HttpError(
                    throwable.code(),
                    message
                )
            }
            else -> RequestError.GenericFailure(
                throwable.message ?: throwable.localizedMessage.orEmpty()
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}