package com.example.homebankfront.feature.editTransactionRow.domain

import com.example.homebankfront.data.bodies.TransactionRow
import com.example.homebankfront.data.repositories.TransactionRowRepository
import javax.inject.Inject

class GetTransactionRowUseCase @Inject constructor(
    private val transactionRowRepository: TransactionRowRepository
) {
    suspend operator fun invoke(transactionHeadId: Long, transactionRowId: Long): TransactionRow {
        return if (transactionRowId == TransactionRow().id) {
            TransactionRow(
                transactionHeadId = transactionHeadId,
                typeOfTransactionCode = TransactionRow.Type.entries[0],
                typeOfTransaction = TransactionRow.Type.entries[0].value
            )
        } else {
            transactionRowRepository.getTransactionRow(transactionRowId = transactionRowId)
        }
    }
}