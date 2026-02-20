package com.example.homebankfront.feature.accountActivationPending

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.homebankfront.R

sealed class AccountActivationPendingError(val stringResourceId: Int) {

    data object ResendEmailFailed : AccountActivationPendingError(R.string.resend_email_failed)

    fun getStringResourceFromContext(context: Context) = context.getString(stringResourceId)

    @Composable
    fun toStringResource(): String = when (this) {
        ResendEmailFailed -> stringResource(stringResourceId)
    }
}