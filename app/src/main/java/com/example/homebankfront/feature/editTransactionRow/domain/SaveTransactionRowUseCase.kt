package com.example.homebankfront.feature.editTransactionRow.domain

import com.example.homebankfront.dataAccess.bodies.TransactionRow
import com.example.homebankfront.dataAccess.repositories.Repository
import javax.inject.Inject

class SaveTransactionRowUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(transactionRow: TransactionRow) =
        repository.saveTransactionRow(transactionRow)
}