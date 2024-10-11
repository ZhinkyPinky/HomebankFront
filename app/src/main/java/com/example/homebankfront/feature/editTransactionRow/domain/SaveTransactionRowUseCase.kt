package com.example.homebankfront.feature.editTransactionRow.domain

import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.bodies.TransactionRow
import com.example.homebankfront.data.repositories.TransactionRowRepository
import com.example.homebankfront.feature.utility.Result
import javax.inject.Inject

class SaveTransactionRowUseCase @Inject constructor(
    private val transactionRowRepository: TransactionRowRepository
) {
    suspend operator fun invoke(transactionRow: TransactionRow) =
        when (val result = validate(transactionRow)) {
            is Result.Failure -> result
            is Result.Success -> {
                transactionRowRepository.saveTransactionRow(transactionRow)
                result
            }
        }

    /**
     * Validates the properties of the [transactionRow] and returns the [Result].
     */
    private fun validate(transactionRow: TransactionRow): Result = when {
        transactionRow.name.isBlank() -> Result.Failure("Titel saknas")
        transactionRow.paymentDate == null -> Result.Failure("Datum saknas")
        transactionRow.typeOfTransactionCode == null || transactionRow.typeOfTransaction == null ->
            Result.Failure("Typ saknas")

        else -> Result.Success
    }
}