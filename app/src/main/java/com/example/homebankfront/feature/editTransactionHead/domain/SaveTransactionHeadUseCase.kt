package com.example.homebankfront.feature.editTransactionHead.domain

import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.repositories.TransactionHeadRepository
import com.example.homebankfront.feature.utility.Result
import javax.inject.Inject


class SaveTransactionHeadUseCase @Inject constructor(
    private val transactionHeadRepository: TransactionHeadRepository
) {
    suspend operator fun invoke(transactionHead: TransactionHead) =
        when (val result = validate(transactionHead)) {
            is Result.Failure -> result
            is Result.Success -> {
                transactionHeadRepository.saveTransactionHead(transactionHead)
                result
            }
        }

    /**
     * Validates the properties of the [transactionHead] and returns the [Result].
     */
    private fun validate(transactionHead: TransactionHead): Result = when {
        transactionHead.transactionName.isNullOrBlank() -> Result.Failure("Titel saknas")
        transactionHead.lenderId == -1L || transactionHead.lender.isNullOrBlank() ->
            Result.Failure("Långivare saknas")

        transactionHead.borrowerId == -1L || transactionHead.borrower.isNullOrBlank() ->
            Result.Failure("Låntagare saknas")

        transactionHead.startDate == null -> Result.Failure("Startdatum saknas")
        else -> Result.Success
    }
}