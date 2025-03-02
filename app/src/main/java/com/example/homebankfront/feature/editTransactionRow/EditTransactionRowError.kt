package com.example.homebankfront.feature.editTransactionRow

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.homebankfront.R

sealed class EditTransactionRowError(val stringResourceId: Int) {
    sealed class NameFieldError(stringResourceId: Int) : EditTransactionRowError(stringResourceId) {
        data object MissingNameError : NameFieldError(R.string.missing_name)
    }
}

fun EditTransactionRowError.getStringResourceFromContext(context: Context) =
    context.getString(stringResourceId)

@Composable
fun EditTransactionRowError.toStringResource(): String = stringResource(stringResourceId)