package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.TransactionRow
import com.example.homebankfront.data.remote.services.TransactionRowService
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.*
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Error.UnknownError
import com.example.homebankfront.feature.utility.NetworkError.SocketTimeOut
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.*
import com.example.homebankfront.feature.utility.logDebug
import java.net.SocketTimeoutException
import javax.inject.Inject

class TransactionRowRepository @Inject constructor(
    private val transactionRowService: TransactionRowService,
    private val responseHandler: ResponseHandler
) {
    suspend fun getTransactionRow(transactionRowId: Long) = runCatching {
        val response = transactionRowService.getTransactionRow(transactionRowId)
        responseHandler(
            response = response,
            onSuccess = { body ->
                logDebug("Successfully retrieved: $body")
                Success(body)
            },
            onFailure = {
                logDebug("Failed to retrieved transaction row with Id: $transactionRowId")
                Failure(Left(Unit))
            }
        )
    }.getOrElse {
        when (it) {
            is SocketTimeoutException -> Failure(Right(SocketTimeOut))
            else -> Failure(Right(UnknownError))
        }
    }

    suspend fun saveTransactionRow(transactionRow: TransactionRow) = runCatching {
        val response = transactionRowService.saveTransactionRow(transactionRow)
        responseHandler(
            response = response,
            onSuccess = { Success(Unit) },
            onFailure = { Failure(Right(UnknownError)) }
        )
    }.getOrElse {
        when (it) {
            is SocketTimeoutException -> Failure(Right(SocketTimeOut))
            else -> Failure(Right(UnknownError))
        }
    }

    suspend fun deleteTransactionRow(transactionRow: TransactionRow) =
        transactionRowService.deleteTransactionRow(transactionRow)

}
