package com.example.homebankfront.dataAccess

import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHead
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHeads
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHeadAndRows
import com.example.homebankfront.dataAccess.bodies.CustomersAndTransactionHead
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.dataAccess.bodies.TransactionRow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {
    @GET("customer")
    suspend fun getCustomers(): List<Customer>

    @GET("customer/transactionHead/{transactionHeadId}")
    suspend fun getCustomersAndTransactionRow(@Path("transactionHeadId") transactionHeadId: Long): CustomersAndTransactionHead

    @GET("customer/{customerId}/transactionHead")
    suspend fun getCustomerAndTransactionHeads(@Path("customerId") customerId: Long): CustomerAndTransactionHeads

    @GET("customer/{customerId}/transactionHead/{transactionHeadId}")
    suspend fun getCustomerAndTransactionHead(
        @Path("customerId") customerId: Long, @Path("transactionHeadId") transactionHeadId: Long
    ): CustomerAndTransactionHead

    @GET("customer/{customerId}/transactionHead/{transactionHeadId}/transactionRow")
    suspend fun getCustomerAndTransactionHeadAndRows(
        @Path("customerId") customerId: Long, @Path("transactionHeadId") transactionHeadId: Long
    ): CustomerAndTransactionHeadAndRows

    @POST("transactionHead")
    suspend fun saveTransactionHead(@Body transactionHead: TransactionHead)

    @DELETE
    suspend fun deleteTransactionHead(@Body transactionHead: TransactionHead)

    @POST("transactionRow")
    suspend fun saveTransactionRow(@Body transactionRow: TransactionRow)

    @DELETE("transactionRow/{transactionRowId}")
    suspend fun deleteTransactionRow(@Path("transactionRowId") transactionRowId: Long)

    @GET("transactionRow/{transactionRowId}")
    suspend fun getTransactionRow(@Path("transactionRowId") transactionRowId: Long): TransactionRow
}