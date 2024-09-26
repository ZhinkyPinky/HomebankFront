package com.example.homebankfront.dataAccess.repositories

import android.util.Log
import com.example.homebankfront.dataAccess.ApiService
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHead
import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHeads
import com.example.homebankfront.dataAccess.bodies.CustomerTransactionHeadAndRows
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.dataAccess.bodies.TransactionRow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService : ApiService
) {

    suspend fun getCustomers() : Flow<List<Customer>> = flow {
        try {
            val customers = apiService.getCustomerAndTransactionHeads()
            emit(customers)
        } catch (e : HttpException) {
            emitError("Network error: ${e.message()}")
        }
    }

    suspend fun getCustomerAndTransactionHeads(customerId : Long) : CustomerAndTransactionHeads {
        return apiService.getCustomerAndTransactionHeads(
            customerId = customerId
        )
    }

    suspend fun getCustomerTransactionHeadAndRows(
        customerId : Long,
        transactionHeadId : Long
    ) : Flow<CustomerTransactionHeadAndRows> = flow {
        try {
            val customerTransactionHeadAndRows = apiService.getCustomerTransactionHeadAndRows(
                customerId = customerId,
                transactionHeadId = transactionHeadId
            )
            emit(customerTransactionHeadAndRows)
        } catch (e : HttpException) {
            emitError("Network error: ${e.message()}")
        }
    }

    suspend fun getCustomerAndTransactionHead(
        customerId : Long,
        transactionHeadId : Long
    ) : Flow<CustomerAndTransactionHead> = flow {
        try {
            val customerTransactionHead = apiService.getCustomerAndTransactionHead(
                customerId = customerId,
                transactionHeadId = transactionHeadId
            )

            emit(customerTransactionHead)
        } catch (e : HttpException) {
            emitError("Network error: ${e.message()}")
        }
    }

    suspend fun getTransactionRow(transactionRowId : Long) : Flow<TransactionRow> = flow {
        try {
            val transactionRow = apiService.getTransactionRow(transactionRowId = transactionRowId)

            emit(transactionRow)
        } catch (e : HttpException) {
            emitError("Network error: ${e.message()}")
        }
    }

    suspend fun saveTransactionHead(transactionHead : TransactionHead) {
        Log.d("boop",
              transactionHead.toString()
        )

        apiService.saveTransactionHead(transactionHead)
    }



    private fun emitError(message : String) : Nothing {
        throw Exception(message)
    }

}
