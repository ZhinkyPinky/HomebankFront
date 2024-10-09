package com.example.homebankfront.feature.transactionHeadAndRows.domain

import com.example.homebankfront.dataAccess.repositories.CustomerRepository
import javax.inject.Inject


class GetCustomerTransactionHeadAndRowsUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(
        customerId: Long,
        transactionHeadId: Long
    ) = customerRepository.getCustomerAndTransactionHeadAndRows(
        customerId = customerId,
        transactionHeadId = transactionHeadId
    )
}
