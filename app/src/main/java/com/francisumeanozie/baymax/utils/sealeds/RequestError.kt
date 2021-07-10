package com.francisumeanozie.baymax.utils.sealeds

sealed class RequestError {
    object NetworkConnection : RequestError()
    data class GenericFailure(val message: String) : RequestError()
    data class HttpError(val statusCode: Int, val message: String?) : RequestError()
}