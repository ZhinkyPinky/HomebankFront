package com.example.homebankfront.feature.editTransactionRow.domain

import com.example.homebankfront.data.bodies.TransactionRow
import com.example.homebankfront.data.repositories.TransactionRowRepository
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.ResultGeneric
import javax.inject.Inject

class GetTransactionRowUseCase @Inject constructor(
    private val transactionRowRepository: TransactionRowRepository
) {
    suspend operator fun invoke(
        transactionHeadId: Long,
        transactionRowId: Long
    ): ResultGeneric<TransactionRow, Either<Unit, Error>> {
        return if (transactionRowId == -1L) {
            ResultGeneric.Success(
                TransactionRow(
                    transactionHeadId = transactionHeadId,
                    typeOfTransactionCode = TransactionRow.Type.entries[0],
                    typeOfTransaction = TransactionRow.Type.entries[0].value
                )
            )
        } else {
            when (val result = transactionRowRepository.getTransactionRow(transactionRowId)) {
                is ResultGeneric.Failure -> result
                is ResultGeneric.Success -> result
            }
        }
    }
}