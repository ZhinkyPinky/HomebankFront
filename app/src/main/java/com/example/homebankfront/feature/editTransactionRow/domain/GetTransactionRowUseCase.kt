package com.example.homebankfront.feature.editTransactionRow.domain

import com.example.homebankfront.dataAccess.bodies.TransactionRow
import com.example.homebankfront.dataAccess.repositories.CustomerRepository
import com.example.homebankfront.dataAccess.repositories.TransactionRowRepository
import javax.inject.Inject

class GetTransactionRowUseCase @Inject constructor(
    private val transactionRowRepository: TransactionRowRepository
) {
    suspend operator fun invoke(transactionHeadId: Long, transactionRowId: Long): TransactionRow {
        if (transactionRowId == TransactionRow().id) {
            return TransactionRow(
                transactionHeadId = transactionHeadId,
                typeOfTransactionCode = TransactionRow.Type.entries[0]
            )
        }

        return transactionRowRepository.getTransactionRow(transactionRowId = transactionRowId)
    }
}