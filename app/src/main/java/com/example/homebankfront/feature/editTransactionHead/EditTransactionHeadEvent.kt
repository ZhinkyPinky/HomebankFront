package com.example.homebankfront.feature.editTransactionHead

import com.example.homebankfront.dataAccess.bodies.TransactionHead

sealed interface EditTransactionHeadEvent {
    data class Update(val transactionHead: TransactionHead) : EditTransactionHeadEvent
    data class Save(val transactionHead: TransactionHead) : EditTransactionHeadEvent
}