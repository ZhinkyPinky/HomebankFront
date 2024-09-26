package com.example.homebankfront.feature.customerList.domain

import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.repositories.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCustomersUseCase @Inject constructor(
    private val repository : Repository
) {
    suspend operator fun invoke() : Flow<List<Customer>> = repository.getCustomers()
}
