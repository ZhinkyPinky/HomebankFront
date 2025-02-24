package com.example.homebankfront.feature.editTransactionHead

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.homebankfront.R

sealed class EditTransactionHeadError(val stringResourceId: Int) {
    data object UnknownEditTransactionHeadError : EditTransactionHeadError(R.string.unknown_error)

    sealed class TransactionNameFieldError(stringResourceId: Int) :
        EditTransactionHeadError(stringResourceId) {
        data object MissingTransactionNameError :
            TransactionNameFieldError(R.string.missing_name)
    }

    sealed class StartDateFieldError(stringResourceId: Int) :
        EditTransactionHeadError(stringResourceId) {
        data object MissingStartDateError : StartDateFieldError(R.string.missing_start_date)
    }

    sealed class BorrowerFieldError(stringResourceId: Int) :
        EditTransactionHeadError(stringResourceId) {
        data object MissingBorrowerError : BorrowerFieldError(R.string.missing_borrower)
    }

    sealed class LenderFieldError(stringResourceId: Int) :
        EditTransactionHeadError(stringResourceId) {
        data object MissingLenderError : LenderFieldError(R.string.missing_lender)
    }
}

fun EditTransactionHeadError.getStringResourceFromContext(context: Context): String =
    context.getString(stringResourceId)

@Composable
fun EditTransactionHeadError.toStringResource(): String = stringResource(stringResourceId)
