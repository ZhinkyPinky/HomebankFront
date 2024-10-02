package com.example.homebankfront.feature.customerTransactionHeadAndRows.domain

import com.example.homebankfront.dataAccess.bodies.CustomerAndTransactionHeadAndRows
import com.example.homebankfront.dataAccess.repositories.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCustomerTransactionHeadAndRowsUseCase @Inject constructor(
    private val repository : Repository
) {
    suspend operator fun invoke(
        customerId : Long,
        transactionHeadId : Long
    ) : Flow<CustomerAndTransactionHeadAndRows> = repository.getCustomerTransactionHeadAndRows(
        customerId,
        transactionHeadId
    )
}
