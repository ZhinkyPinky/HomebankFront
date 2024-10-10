package com.example.homebankfront.data.services

import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.data.bodies.CustomerAndTransactionHead
import com.example.homebankfront.data.bodies.CustomerAndTransactionHeads
import com.example.homebankfront.data.bodies.CustomerAndTransactionHeadAndRows
import com.example.homebankfront.data.bodies.CustomersAndTransactionHead
import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.bodies.TransactionRow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("customers")
    suspend fun getCustomers(): List<Customer>

    @GET("customers/transactionHeads/{transactionHeadId}")
    suspend fun getCustomersAndTransactionHead(@Path("transactionHeadId") transactionHeadId: Long): CustomersAndTransactionHead

    @GET("customers/{customerId}/transactionHeads")
    suspend fun getCustomerAndTransactionHeads(@Path("customerId") customerId: Long): CustomerAndTransactionHeads

    @GET("customers/{customerId}/transactionHeads/{transactionHeadId}")
    suspend fun getCustomerAndTransactionHead(
        @Path("customerId") customerId: Long, @Path("transactionHeadId") transactionHeadId: Long
    ): CustomerAndTransactionHead

    @GET("customers/{customerId}/transactionHeads/{transactionHeadId}/transactionRows")
    suspend fun getCustomerAndTransactionHeadAndRows(
        @Path("customerId") customerId: Long, @Path("transactionHeadId") transactionHeadId: Long
    ): CustomerAndTransactionHeadAndRows

    @POST("transactionHeads?action=save")
    suspend fun saveTransactionHead(
        @Body transactionHead: TransactionHead
    )

    @POST("transactionHeads?action=delete")
    suspend fun deleteTransactionHead(@Body transactionHead: TransactionHead)

    @POST("transactionRows?action=save")
    suspend fun saveTransactionRow(
        @Body transactionRow: TransactionRow)

    @POST("transactionRows?action=delete")
    suspend fun deleteTransactionRow(@Body transactionRow: TransactionRow)

    @GET("transactionRows/{transactionRowId}")
    suspend fun getTransactionRow(@Path("transactionRowId") transactionRowId: Long): TransactionRow
}