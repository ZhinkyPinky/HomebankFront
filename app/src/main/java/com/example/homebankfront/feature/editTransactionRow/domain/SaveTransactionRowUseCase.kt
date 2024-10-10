package com.example.homebankfront.feature.editTransactionRow.domain

import com.example.homebankfront.data.bodies.TransactionRow
import com.example.homebankfront.data.repositories.TransactionRowRepository
import javax.inject.Inject

class SaveTransactionRowUseCase @Inject constructor(
    private val transactionRowRepository: TransactionRowRepository
) {
    suspend operator fun invoke(transactionRow: TransactionRow) =
        transactionRowRepository.saveTransactionRow(transactionRow)
}