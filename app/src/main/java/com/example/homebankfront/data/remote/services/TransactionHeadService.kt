package com.example.homebankfront.data.remote.services

import com.example.homebankfront.data.bodies.TransactionHead
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TransactionHeadService {
    @POST(ApiPaths.SAVE_TRANSACTION_HEAD)
    suspend fun saveTransactionHead(@Body transactionHead: TransactionHead): Response<Unit>

    @POST(ApiPaths.DELETE_TRANSACTION_HEAD)
    suspend fun deleteTransactionHead(@Body transactionHead: TransactionHead): Response<Unit>
}