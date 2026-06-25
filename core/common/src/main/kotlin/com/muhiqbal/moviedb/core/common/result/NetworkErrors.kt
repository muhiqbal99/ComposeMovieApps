package com.muhiqbal.moviedb.core.common.result

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NoConnectivityException(cause: Throwable? = null) :
    IOException("No internet connection", cause)

fun Throwable.toAppException(): Throwable = when (this) {
    is NoConnectivityException -> this
    is UnknownHostException,
    is ConnectException,
    is SocketTimeoutException,
    -> NoConnectivityException(this)
    is IOException -> NoConnectivityException(this)
    else -> this
}
