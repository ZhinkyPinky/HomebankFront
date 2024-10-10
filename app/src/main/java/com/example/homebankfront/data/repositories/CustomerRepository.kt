package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.services.ApiService
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getCustomers() = apiService.getCustomers()

    suspend fun getCustomersAndTransactionHead(transactionHeadId: Long) =
        apiService.getCustomersAndTransactionHead(transactionHeadId = transactionHeadId)

    suspend fun getCustomerAndTransactionHead(customerId: Long, transactionHeadId: Long) =
        apiService.getCustomerAndTransactionHead(
            customerId = customerId,
            transactionHeadId = transactionHeadId
        )

    suspend fun getCustomerAndTransactionHeads(customerId: Long) =
        apiService.getCustomerAndTransactionHeads(customerId = customerId)

    suspend fun getCustomerAndTransactionHeadAndRows(customerId: Long, transactionHeadId: Long) =
        apiService.getCustomerAndTransactionHeadAndRows(
            customerId = customerId,
            transactionHeadId = transactionHeadId
        )
}
