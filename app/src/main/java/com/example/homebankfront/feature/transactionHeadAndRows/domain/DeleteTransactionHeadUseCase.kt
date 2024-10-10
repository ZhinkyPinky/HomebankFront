package com.example.homebankfront.feature.transactionHeadAndRows.domain

import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.repositories.TransactionHeadRepository
import javax.inject.Inject

class DeleteTransactionHeadUseCase @Inject constructor(
    private val transactionHeadRepository: TransactionHeadRepository
) {
    suspend operator fun invoke(transactionHead: TransactionHead) =
        transactionHeadRepository.deleteTransactionHead(transactionHead)
}