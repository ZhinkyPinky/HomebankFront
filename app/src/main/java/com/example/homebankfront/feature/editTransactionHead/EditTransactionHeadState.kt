package com.example.homebankfront.feature.editTransactionHead

import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.TransactionHead

sealed interface EditTransactionHeadState {
    data object Loading : EditTransactionHeadState

    data class Ready(
        val transactionHead: TransactionHead,
        val customers: List<Customer>
    ) : EditTransactionHeadState

    data object Saved : EditTransactionHeadState
}