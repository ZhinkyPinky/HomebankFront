package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.services.ApiService
import com.example.homebankfront.data.bodies.TransactionHead
import javax.inject.Inject

class TransactionHeadRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun saveTransactionHead(transactionHead: TransactionHead) =
        apiService.saveTransactionHead(transactionHead)

    suspend fun deleteTransactionHead(transactionHead: TransactionHead) =
        apiService.deleteTransactionHead(transactionHead)
}