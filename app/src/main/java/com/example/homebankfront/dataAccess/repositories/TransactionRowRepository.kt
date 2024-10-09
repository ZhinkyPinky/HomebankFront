package com.example.homebankfront.dataAccess.repositories

import com.example.homebankfront.dataAccess.ApiService
import com.example.homebankfront.dataAccess.bodies.TransactionRow
import retrofit2.HttpException
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