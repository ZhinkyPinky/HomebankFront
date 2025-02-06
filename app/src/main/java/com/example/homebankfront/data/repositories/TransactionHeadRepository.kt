package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.remote.services.TransactionHeadService
import javax.inject.Inject

class TransactionHeadRepository @Inject constructor(
    private val transactionHeadService: TransactionHeadService
) {
    suspend fun saveTransactionHead(transactionHead: TransactionHead) =
        transactionHeadService.saveTransactionHead(transactionHead)

    suspend fun deleteTransactionHead(transactionHead: TransactionHead) =
        transactionHeadService.deleteTransactionHead(transactionHead)
}