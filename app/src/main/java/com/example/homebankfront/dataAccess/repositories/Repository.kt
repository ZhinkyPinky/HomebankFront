package com.example.homebankfront.dataAccess.repositories

import com.example.homebankfront.dataAccess.ApiService
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHead
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHeads
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHeadAndRows
import com.example.homebankfront.dataAccess.bodies.CustomersAndTransactionHead
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.dataAccess.bodies.TransactionRow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getCustomers(): Flow<List<Customer>> = flow {
        try {
            val customers = apiService.getCustomers()
            emit(customers)
        } catch (e: HttpException) {
            emitError("Network error: ${e.message()}")
        }
    }

    suspend fun getCustomersAndTransactionHead(transactionHeadId: Long): Flow<CustomersAndTransactionHead> =
        flow {
            try {
                val customersAndTransactionHead =
                    apiService.getCustomersAndTransactionRow(transactionHeadId)
                emit(customersAndTransactionHead)
            } catch (e: HttpException) {
                emitError("NetworkError: ${e.message()}")
            }
        }

    suspend fun getCustomerAndTransactionHeads(customerId: Long): CustomerAndTransactionHeads {
        return apiService.getCustomerAndTransactionHeads(
            customerId = customerId
        )
    }

    suspend fun getCustomerTransactionHeadAndRows(
        customerId: Long,
        transactionHeadId: Long
    ): Flow<CustomerAndTransactionHeadAndRows> = flow {
        try {
            val customerTransactionHeadAndRows = apiService.getCustomerAndTransactionHeadAndRows(
                customerId = customerId,
                transactionHeadId = transactionHeadId
            )
            emit(customerTransactionHeadAndRows)
        } catch (e: HttpException) {
            emitError("Network error: ${e.message()}")
        }
    }

    suspend fun getCustomerAndTransactionHead(
        customerId: Long,
        transactionHeadId: Long
    ): Flow<CustomerAndTransactionHead> = flow {
        try {
            val customerTransactionHead = apiService.getCustomerAndTransactionHead(
                customerId = customerId,
                transactionHeadId = transactionHeadId
            )

            emit(customerTransactionHead)
        } catch (e: HttpException) {
            emitError("Network error: ${e.message()}")
        }
    }

    suspend fun getTransactionRow(transactionRowId: Long): Flow<TransactionRow> = flow {
        try {
            val transactionRow = apiService.getTransactionRow(transactionRowId = transactionRowId)

            emit(transactionRow)
        } catch (e: HttpException) {
            emitError("Network error: ${e.message()}")
        }
    }

    suspend fun saveTransactionHead(transactionHead: TransactionHead) {
        apiService.saveTransactionHead(transactionHead)
    }

    suspend fun saveTransactionRow(transactionRow: TransactionRow) {
        apiService.saveTransactionRow(transactionRow)
    }

    suspend fun deleteTransactionRow(transactionRowId: Long) {
        apiService.deleteTransactionRow(transactionRowId)
    }
    private fun emitError(message: String): Nothing {
        throw Exception(message)
    }
}
