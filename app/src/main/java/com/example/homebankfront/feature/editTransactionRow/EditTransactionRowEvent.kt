package com.example.homebankfront.feature.editTransactionRow

import com.example.homebankfront.dataAccess.bodies.TransactionRow

sealed interface EditTransactionRowEvent {
    data class Update(val transactionRow: TransactionRow) : EditTransactionRowEvent
    data class Save(val transactionRow: TransactionRow) : EditTransactionRowEvent
}