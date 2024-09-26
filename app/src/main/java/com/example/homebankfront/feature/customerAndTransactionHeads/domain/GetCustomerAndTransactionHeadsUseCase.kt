package com.example.homebankfront.feature.customerAndTransactionHeads.domain

import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHeads
import com.example.homebankfront.dataAccess.repositories.Repository
import javax.inject.Inject


class GetCustomerAndTransactionHeadsUseCase @Inject constructor(
    private val repository : Repository
) {
    suspend operator fun invoke(id : Long) : CustomerAndTransactionHeads {
        return repository.getCustomerAndTransactionHeads(customerId = id)
    }
}
