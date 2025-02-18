package com.example.homebankfront.feature.editTransactionHead

import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadError.*
import java.time.LocalDate

sealed interface EditTransactionHeadState {
    data object Loading : EditTransactionHeadState

    data class Ready(
        val transactionHead: TransactionHead,
        val customers: List<Customer>
    ) : EditTransactionHeadState

    data object Saved : EditTransactionHeadState
}

sealed interface EditTransactionHeadField {
    data class TransactionName(
        val transactionName: String,
        val error: TransactionNameFieldError? = null
    ) : EditTransactionHeadField

    data class Description(val description: String) : EditTransactionHeadField
    data class StartDate(val startDate: LocalDate) : EditTransactionHeadField
    data class PrelEndDate(val prelEndDate: LocalDate) : EditTransactionHeadField
    data class EndDate(val endDate: LocalDate) : EditTransactionHeadField
    data class Lender(
        val lenderId: Long,
        val lender: String
    ) : EditTransactionHeadField

    data class Borrower(
        val borrowerId: Long,
        val borrower: String
    ) : EditTransactionHeadField
}

sealed class EditTransactionHeadError(val stringResourceId: Int) {
    sealed class TransactionNameFieldError(stringResourceId: Int) :
        EditTransactionHeadError(stringResourceId) {
        data object MissingTransactionName : TransactionNameFieldError(R.string.missing_transaction_name)
    }
}
