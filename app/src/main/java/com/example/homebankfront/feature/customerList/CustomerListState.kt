package com.example.homebankfront.feature.customerList

import com.example.homebankfront.data.bodies.Customer

sealed interface CustomerListState {
    data object Loading : CustomerListState
    data class Ready(val customers: List<Customer>) : CustomerListState
}