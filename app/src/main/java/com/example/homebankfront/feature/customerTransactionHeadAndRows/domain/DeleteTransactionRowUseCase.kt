package com.example.homebankfront.feature.customerTransactionHeadAndRows.domain

import com.example.homebankfront.dataAccess.bodies.TransactionRow
import com.example.homebankfront.dataAccess.repositories.Repository
import javax.inject.Inject

class DeleteTransactionRowUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(transactionRowId : Long) = repository.deleteTransactionRow(transactionRowId)
}