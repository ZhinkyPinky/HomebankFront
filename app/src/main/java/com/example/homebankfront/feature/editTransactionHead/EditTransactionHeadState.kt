package com.example.homebankfront.feature.editTransactionHead

import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.data.bodies.TransactionHead

sealed interface EditTransactionHeadState {
    data object Loading : EditTransactionHeadState

    data class Ready(
        val transactionHead: TransactionHead,
        val customers: List<Customer>
    ) : EditTransactionHeadState

    data object Saved : EditTransactionHeadState
}