package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.data.bodies.CustomerAndTransactionHeads
import com.example.homebankfront.data.remote.services.CustomerService
import com.example.homebankfront.feature.customerList.CustomerError
import com.example.homebankfront.feature.customerList.toCustomerError
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.*
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Error.UnknownError
import com.example.homebankfront.feature.utility.Logger
import com.example.homebankfront.feature.utility.NetworkError.SocketTimeOut
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
import java.net.SocketTimeoutException
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val customerService: CustomerService,
    private val responseHandler: ResponseHandler
) {
    suspend fun getCustomers(): ResultGeneric<List<Customer>, Either<CustomerError, Error>> =
        runCatching {
            val response = customerService.getCustomers()
            responseHandler(
                response = response,
                onSuccess = { body ->
                    Logger.d(message = "Successfully retrieved: $body")
                    Success(body)
                },
                onFailure = { errorMessage: String? -> Failure(errorMessage.toCustomerError()) }
            )
        }.getOrElse {
            when (it) {
                is SocketTimeoutException -> Failure(Right(SocketTimeOut))
                else -> Failure(Right(UnknownError))
            }
        }


    suspend fun getCustomersAndTransactionHead(transactionHeadId: Long) =
        customerService.getCustomersAndTransactionHead(transactionHeadId = transactionHeadId)

    suspend fun getCustomerAndTransactionHead(customerId: Long, transactionHeadId: Long) =
        customerService.getCustomerAndTransactionHead(
            customerId = customerId,
            transactionHeadId = transactionHeadId
        )

    suspend fun getCustomerAndTransactionHeads(customerId: Long): ResultGeneric<CustomerAndTransactionHeads, Either<Unit, Error>> =
        runCatching {
            val response = customerService.getCustomerAndTransactionHeads(customerId = customerId)
            responseHandler(
                response = response,
                onSuccess = { body ->
                    Success(body)
                },
                onFailure = {
                    Failure(Left(Unit))
                }
            )
        }.getOrElse {
            when (it) {
                is SocketTimeoutException -> Failure(Right(SocketTimeOut))
                else -> Failure(Right(UnknownError))
            }
        }

    suspend fun getCustomerAndTransactionHeadAndRows(customerId: Long, transactionHeadId: Long) =
        customerService.getCustomerTransactionHeadAndRows(
            customerId = customerId,
            transactionHeadId = transactionHeadId
        )
}
