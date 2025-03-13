package com.example.homebankfront.data.remote.services

import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.data.bodies.CustomerAndTransactionHead
import com.example.homebankfront.data.bodies.CustomerAndTransactionHeads
import com.example.homebankfront.data.bodies.CustomerAndTransactionHeadAndRows
import com.example.homebankfront.data.bodies.CustomersAndTransactionHead
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface CustomerService {
    @GET(ApiPaths.CUSTOMERS)
    suspend fun getCustomers(): Response<List<Customer>>

    @GET(ApiPaths.CUSTOMERS_AND_TRANSACTION_HEAD)
    suspend fun getCustomersAndTransactionHead(@Path("transactionHeadId") transactionHeadId: Long): CustomersAndTransactionHead

    @GET(ApiPaths.CUSTOMER_AND_TRANSACTION_HEADS)
    suspend fun getCustomerAndTransactionHeads(@Path("customerId") customerId: Long): Response<CustomerAndTransactionHeads>

    @GET(ApiPaths.CUSTOMER_AND_TRANSACTION_HEAD)
    suspend fun getCustomerAndTransactionHead(
        @Path("customerId") customerId: Long,
        @Path("transactionHeadId") transactionHeadId: Long
    ): CustomerAndTransactionHead

    @GET(ApiPaths.CUSTOMER_TRANSACTION_HEAD_AND_ROWS)
    suspend fun getCustomerTransactionHeadAndRows(
        @Path("customerId") customerId: Long,
        @Path("transactionHeadId") transactionHeadId: Long
    ): CustomerAndTransactionHeadAndRows
}