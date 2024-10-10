package com.example.homebankfront.feature.transactionHeadAndRows

import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.bodies.TransactionRow

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
