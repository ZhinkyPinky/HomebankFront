package com.example.homebankfront.data.repositories

import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.remote.services.TransactionHeadService
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadError
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadError.UnknownEditTransactionHeadError
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.Error.UnknownError
import com.example.homebankfront.feature.utility.NetworkError.SocketTimeOut
import com.example.homebankfront.feature.utility.ResultGeneric
import java.net.SocketTimeoutException
import javax.inject.Inject

class TransactionHeadRepository @Inject constructor(
    private val transactionHeadService: TransactionHeadService,
    private val responseHandler: ResponseHandler
) {
    //TODO: Improve error handling.
    suspend fun saveTransactionHead(transactionHead: TransactionHead): ResultGeneric<Unit, Either<EditTransactionHeadError, Error>> {
        return runCatching {
            val response = transactionHeadService.saveTransactionHead(transactionHead)
            responseHandler(
                response = response,
                onSuccess = { ResultGeneric.Success(Unit) },
                onFailure = { ResultGeneric.Failure(Left(UnknownEditTransactionHeadError)) }
            )
        }.getOrElse {
            when (it) {
                is SocketTimeoutException -> ResultGeneric.Failure(Right(SocketTimeOut))
                else -> ResultGeneric.Failure(Right(UnknownError))
            }
        }
    }

    suspend fun deleteTransactionHead(transactionHead: TransactionHead) =
        transactionHeadService.deleteTransactionHead(transactionHead)
}