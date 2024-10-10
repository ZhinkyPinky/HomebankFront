package com.example.homebankfront.feature.editTransactionHead.domain

import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.repositories.TransactionHeadRepository
import javax.inject.Inject


class SaveTransactionHeadUseCase @Inject constructor(
    private val transactionHeadRepository: TransactionHeadRepository
) {
    suspend operator fun invoke(transactionHead: TransactionHead) =
        transactionHeadRepository.saveTransactionHead(transactionHead)
}