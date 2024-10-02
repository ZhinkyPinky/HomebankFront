package com.example.homebankfront.feature.editTransactionRow.domain

import com.example.homebankfront.dataAccess.repositories.Repository
import javax.inject.Inject

class GetCustomersAndTransactionHeadUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(transactionHeadId: Long) = repository.getCustomersAndTransactionHead(transactionHeadId)
}