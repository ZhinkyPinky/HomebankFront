package com.example.homebankfront.feature.customerTransactionHeadAndRows.domain

import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.dataAccess.repositories.Repository
import javax.inject.Inject

class DeleteTransactionHeadUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(transactionHead: TransactionHead) =
        repository.deleteTransactionHead(transactionHead)
}