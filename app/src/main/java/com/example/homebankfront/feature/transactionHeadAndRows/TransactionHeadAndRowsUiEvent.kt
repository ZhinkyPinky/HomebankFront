package com.example.homebankfront.feature.transactionHeadAndRows

import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.bodies.TransactionRow

sealed interface TransactionHeadAndRowsUiEvent {
    data class DeleteTransactionHeadUi(val transactionHead: TransactionHead) :
        TransactionHeadAndRowsUiEvent

    data class DeleteRow(val transactionRow: TransactionRow) : TransactionHeadAndRowsUiEvent
}