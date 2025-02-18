package com.example.homebankfront.feature.customerList

import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error

data class CustomerListState(
    val customers: List<Customer> = emptyList(),
    val isLoading: Boolean = true
)

sealed class CustomerError(val stringResourceId: Int) {
    data object NoCustomersFound
}

fun String?.toCustomerError(): Either<CustomerError, Error> = when (this) {
    else -> Either.Right(Error.UnknownError)
}
