package com.example.homebankfront.feature.editTransactionRow

import com.example.homebankfront.data.bodies.TransactionRow

sealed interface EditTransactionRowState {
    data object Loading : EditTransactionRowState
    data class Ready(val transactionRow: TransactionRow) : EditTransactionRowState
    data object Saved : EditTransactionRowState
}