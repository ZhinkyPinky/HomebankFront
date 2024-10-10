package com.example.homebankfront.feature.editTransactionHead.domain

import com.example.homebankfront.data.repositories.CustomerRepository
import javax.inject.Inject

class GetCustomerAndTransactionHeadUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(
        customerId: Long,
        transactionHeadId: Long
    ) = customerRepository.getCustomerAndTransactionHead(
        customerId,
        transactionHeadId
    )
}
