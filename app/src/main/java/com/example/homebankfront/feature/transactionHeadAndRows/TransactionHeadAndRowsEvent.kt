package com.example.homebankfront.feature.transactionHeadAndRows

import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.dataAccess.bodies.TransactionRow

sealed interface TransactionHeadAndRowsEvent {
    data class DeleteTransactionHead(val transactionHead: TransactionHead) :
        TransactionHeadAndRowsEvent

    data class DeleteRow(val transactionRow: TransactionRow) : TransactionHeadAndRowsEvent
}