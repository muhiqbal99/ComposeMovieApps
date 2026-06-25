package com.muhiqbal.moviedb.core.ui.error

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.muhiqbal.moviedb.core.common.result.NoConnectivityException
import com.muhiqbal.moviedb.core.ui.R

@Composable
fun errorMessageFor(error: Throwable?): String = when (error) {
    is NoConnectivityException -> stringResource(R.string.error_no_internet)
    else -> stringResource(R.string.error_generic)
}
