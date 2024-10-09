package com.example.homebankfront.feature.transactionHeadsList

import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.TransactionHead


sealed interface TransactionHeadsListState {
    data object Loading : TransactionHeadsListState

    data class Ready(
        val customer: Customer,
        val transactionHeads: List<TransactionHead>
    ) : TransactionHeadsListState
}