package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.TransactionRow
import com.example.homebankfront.data.remote.services.TransactionRowService
import javax.inject.Inject

class TransactionRowRepository @Inject constructor(
    private val transactionRowService: TransactionRowService
) {
    suspend fun getTransactionRow(transactionRowId: Long): TransactionRow =
        transactionRowService.getTransactionRow(transactionRowId = transactionRowId)

    suspend fun saveTransactionRow(transactionRow: TransactionRow) =
        transactionRowService.saveTransactionRow(transactionRow)

    suspend fun deleteTransactionRow(transactionRow: TransactionRow) =
        transactionRowService.deleteTransactionRow(transactionRow)

}