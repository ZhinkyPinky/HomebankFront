package com.example.homebankfront.feature.editTransactionHead.domain

import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHead
import com.example.homebankfront.dataAccess.repositories.CustomerRepository
import kotlinx.coroutines.flow.Flow
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
