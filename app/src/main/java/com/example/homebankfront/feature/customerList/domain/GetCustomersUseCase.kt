package com.example.homebankfront.feature.customerList.domain

import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.repositories.CustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCustomersUseCase @Inject constructor(
    private val customerRepository : CustomerRepository
) {
    suspend operator fun invoke() : List<Customer> = customerRepository.getCustomers()
}
