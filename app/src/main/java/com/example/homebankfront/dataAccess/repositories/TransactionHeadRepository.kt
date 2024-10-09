package com.example.homebankfront.dataAccess.repositories

import com.example.homebankfront.dataAccess.ApiService
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import javax.inject.Inject

class TransactionHeadRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun saveTransactionHead(transactionHead: TransactionHead) =
        apiService.saveTransactionHead(transactionHead)

    suspend fun deleteTransactionHead(transactionHead: TransactionHead) =
        apiService.deleteTransactionHead(transactionHead)
}