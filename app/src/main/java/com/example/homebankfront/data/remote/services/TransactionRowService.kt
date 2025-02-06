package com.example.homebankfront.data.remote.services

import com.example.homebankfront.data.bodies.TransactionRow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TransactionRowService{
    @GET(ApiPaths.TRANSACTION_ROW)
    suspend fun getTransactionRow(@Path("transactionRowId") transactionRowId: Long): TransactionRow

    @POST(ApiPaths.SAVE_TRANSACTION_ROW)
    suspend fun saveTransactionRow(
        @Body transactionRow: TransactionRow
    )

    @POST(ApiPaths.DELETE_TRANSACTION_ROW)
    suspend fun deleteTransactionRow(@Body transactionRow: TransactionRow)
}