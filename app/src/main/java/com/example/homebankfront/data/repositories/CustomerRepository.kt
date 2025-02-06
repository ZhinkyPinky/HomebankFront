package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.remote.services.CustomerService
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val customerService: CustomerService
) {
    suspend fun getCustomers() = customerService.getCustomers()

    suspend fun getCustomersAndTransactionHead(transactionHeadId: Long) =
        customerService.getCustomersAndTransactionHead(transactionHeadId = transactionHeadId)

    suspend fun getCustomerAndTransactionHead(customerId: Long, transactionHeadId: Long) =
        customerService.getCustomerAndTransactionHead(
            customerId = customerId,
            transactionHeadId = transactionHeadId
        )

    suspend fun getCustomerAndTransactionHeads(customerId: Long) =
        customerService.getCustomerAndTransactionHeads(customerId = customerId)

    suspend fun getCustomerAndTransactionHeadAndRows(customerId: Long, transactionHeadId: Long) =
        customerService.getCustomerTransactionHeadAndRows(
            customerId = customerId,
            transactionHeadId = transactionHeadId
        )
}
