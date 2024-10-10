package com.example.homebankfront.feature.transactionHeadsList.domain

import com.example.homebankfront.data.repositories.CustomerRepository
import javax.inject.Inject


class GetCustomerAndTransactionHeadsUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(id: Long) =
        customerRepository.getCustomerAndTransactionHeads(customerId = id)
}
