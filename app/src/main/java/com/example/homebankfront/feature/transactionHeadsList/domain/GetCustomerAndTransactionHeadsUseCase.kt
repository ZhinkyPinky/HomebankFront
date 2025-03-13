package com.example.homebankfront.feature.transactionHeadsList.domain

import com.example.homebankfront.data.bodies.CustomerAndTransactionHeads
import com.example.homebankfront.data.repositories.CustomerRepository
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.ResultGeneric
import javax.inject.Inject


class GetCustomerAndTransactionHeadsUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(id: Long): ResultGeneric<CustomerAndTransactionHeads, Either<Unit, Error>> =
        customerRepository.getCustomerAndTransactionHeads(customerId = id)
}
