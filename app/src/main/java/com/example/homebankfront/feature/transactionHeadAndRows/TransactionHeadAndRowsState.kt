package com.example.homebankfront.feature.transactionHeadAndRows

import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.dataAccess.bodies.TransactionRow

sealed interface TransactionHeadAndRowsState {
    data object Loading : TransactionHeadAndRowsState

    data class Ready(
        val customer: Customer,
        val transactionHead: TransactionHead,
        val transactionRows: List<TransactionRow>
    ) : TransactionHeadAndRowsState

    data object Deleted : TransactionHeadAndRowsState

    data class Error(val message: String?) : TransactionHeadAndRowsState
}
