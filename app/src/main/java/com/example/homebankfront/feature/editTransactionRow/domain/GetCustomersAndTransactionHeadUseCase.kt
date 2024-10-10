package com.example.homebankfront.feature.editTransactionRow.domain

import com.example.homebankfront.data.bodies.CustomersAndTransactionHead
import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.repositories.CustomerRepository
import javax.inject.Inject

class GetCustomersAndTransactionHeadUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(transactionHeadId: Long) : CustomersAndTransactionHead {
        if (transactionHeadId == -1L) {
            return CustomersAndTransactionHead(
                customers = customerRepository.getCustomers(),
                transactionHead = TransactionHead()
            )
        }

       return  customerRepository.getCustomersAndTransactionHead(transactionHeadId)
    }
}