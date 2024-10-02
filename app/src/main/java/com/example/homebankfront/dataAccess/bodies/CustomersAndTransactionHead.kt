package com.example.homebankfront.dataAccess.bodies

data class CustomersAndTransactionHead (
    val customers: List<Customer> = listOf(),
    val transactionHead: TransactionHead
)
