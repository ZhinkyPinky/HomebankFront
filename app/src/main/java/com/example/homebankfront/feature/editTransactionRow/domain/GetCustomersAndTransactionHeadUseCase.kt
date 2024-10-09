package com.example.homebankfront.feature.editTransactionRow.domain

import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHead
import com.example.homebankfront.dataAccess.bodies.CustomersAndTransactionHead
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.dataAccess.repositories.CustomerRepository
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