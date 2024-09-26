package com.example.homebankfront.feature.editTransactionRow.domain

import com.example.homebankfront.dataAccess.bodies.TransactionRow
import com.example.homebankfront.dataAccess.repositories.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionRowUseCase @Inject constructor(
    private val repository : Repository
) {
    suspend operator fun invoke(transactionRowId : Long) : Flow<TransactionRow> = repository.getTransactionRow(transactionRowId = transactionRowId)
}