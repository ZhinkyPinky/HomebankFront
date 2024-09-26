package com.example.homebankfront.dataAccess

import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHead
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHeads
import com.example.homebankfront.dataAccess.bodies.CustomerTransactionHeadAndRows
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.dataAccess.bodies.TransactionRow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {
    @GET("customer")
    suspend fun getCustomerAndTransactionHeads() : List<Customer>

    @GET("customer/{customerId}/transactionHead")
    suspend fun getCustomerAndTransactionHeads(@Path("customerId") customerId : Long) : CustomerAndTransactionHeads

    @GET("customer/{customerId}/transactionHead/{transactionHeadId}")
    suspend fun getCustomerAndTransactionHead(
        @Path("customerId") customerId : Long,
        @Path("transactionHeadId") transactionHeadId : Long
    ) : CustomerAndTransactionHead

    @GET("customer/{customerId}/transactionHead/{transactionHeadId}/transactionRow")
    suspend fun getCustomerTransactionHeadAndRows(
        @Path("customerId") customerId : Long,
        @Path("transactionHeadId") transactionHeadId : Long
    ) : CustomerTransactionHeadAndRows

    @GET("customer/{customerId}/transactionHead/{transactionHeadId}/transactionRow/{transactionRowId}")
    suspend fun getTransactionRow(transactionRowId : Long) : TransactionRow

    @POST("transactionHead")
    suspend fun saveTransactionHead(@Body transactionHead : TransactionHead)
}