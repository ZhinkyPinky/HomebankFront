package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.services.ApiService
import com.example.homebankfront.data.bodies.TransactionRow
import javax.inject.Inject

class TransactionRowRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getTransactionRow(transactionRowId: Long): TransactionRow =
        apiService.getTransactionRow(transactionRowId = transactionRowId)

    suspend fun saveTransactionRow(transactionRow: TransactionRow) =
        apiService.saveTransactionRow(transactionRow)

    suspend fun deleteTransactionRow(transactionRow: TransactionRow) =
        apiService.deleteTransactionRow(transactionRow)

}