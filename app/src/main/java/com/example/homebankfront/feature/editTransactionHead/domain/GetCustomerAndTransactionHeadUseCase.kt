package com.example.homebankfront.feature.editTransactionHead.domain

import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHead
import com.example.homebankfront.dataAccess.repositories.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCustomerAndTransactionHeadUseCase @Inject constructor(
    private val repository : Repository
) {
    suspend operator fun invoke(
        customerId : Long,
        transactionHeadId : Long
    ) : Flow<CustomerAndTransactionHead> = repository.getCustomerAndTransactionHead(
        customerId,
        transactionHeadId
    )
}
