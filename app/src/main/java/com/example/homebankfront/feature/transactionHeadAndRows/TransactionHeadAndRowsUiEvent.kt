package com.example.homebankfront.feature.transactionHeadAndRows

import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.bodies.TransactionRow

sealed interface TransactionHeadAndRowsUiEvent {
    data class DeleteTransactionHead(val transactionHead: TransactionHead) :
        TransactionHeadAndRowsUiEvent

    data class DeleteRow(val transactionRow: TransactionRow) : TransactionHeadAndRowsUiEvent
}

fun delete(
    onEvent: (TransactionHeadAndRowsUiEvent) -> Unit,
    transactionHead: TransactionHead
) {
    onEvent(TransactionHeadAndRowsUiEvent.DeleteTransactionHead(transactionHead = transactionHead))
}

fun delete(
    onEvent: (TransactionHeadAndRowsUiEvent) -> Unit,
    transactionRow: TransactionRow
) {
    onEvent(TransactionHeadAndRowsUiEvent.DeleteRow(transactionRow = transactionRow))
}