package com.example.homebankfront.feature.editTransactionRow.domain

import com.example.homebankfront.data.bodies.CustomersAndTransactionHead
import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.repositories.CustomerRepository
import com.example.homebankfront.feature.customerList.CustomerError
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.ResultGeneric
import javax.inject.Inject

class GetCustomersAndTransactionHeadUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(transactionHeadId: Long): ResultGeneric<CustomersAndTransactionHead, Either<CustomerError, Error>> {
        return if (transactionHeadId == -1L) {
            when (val result = customerRepository.getCustomers()) {
                is ResultGeneric.Failure -> result
                is ResultGeneric.Success -> ResultGeneric.Success(
                    CustomersAndTransactionHead(
                        customers = result.data,
                        transactionHead = TransactionHead()
                    )
                )
            }

        } else {
            //TODO: Complete.
            ResultGeneric.Success(
                customerRepository.getCustomersAndTransactionHead(transactionHeadId)
            )
        }
    }
}