package com.example.homebankfront.feature.transactionHeadAndRows.domain

import com.example.homebankfront.dataAccess.bodies.TransactionRow
import com.example.homebankfront.dataAccess.repositories.TransactionRowRepository
import javax.inject.Inject

class DeleteTransactionRowUseCase @Inject constructor(
    private val transactionRowRepository: TransactionRowRepository
) {
    suspend operator fun invoke(transactionRow: TransactionRow) =
        transactionRowRepository.deleteTransactionRow(transactionRow)
}