package com.example.homebankfront.dataAccess.repositories

import com.example.homebankfront.dataAccess.ApiService
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHead
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHeads
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHeadAndRows
import com.example.homebankfront.dataAccess.bodies.CustomersAndTransactionHead
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getCustomers() = apiService.getCustomers()

    suspend fun getCustomersAndTransactionHead(transactionHeadId: Long) =
        apiService.getCustomersAndTransactionRow(transactionHeadId = transactionHeadId)

    suspend fun getCustomerAndTransactionHead(customerId: Long, transactionHeadId: Long) =
        apiService.getCustomerAndTransactionHead(
            customerId = customerId, transactionHeadId = transactionHeadId
        )

    suspend fun getCustomerAndTransactionHeads(customerId: Long) =
        apiService.getCustomerAndTransactionHeads(customerId = customerId)


    suspend fun getCustomerAndTransactionHeadAndRows(customerId: Long, transactionHeadId: Long) =
        apiService.getCustomerAndTransactionHeadAndRows(
            customerId = customerId, transactionHeadId = transactionHeadId
        )
}
