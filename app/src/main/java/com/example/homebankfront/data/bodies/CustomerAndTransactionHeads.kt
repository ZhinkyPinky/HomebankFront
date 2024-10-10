package com.example.homebankfront.data.bodies


data class CustomerAndTransactionHeads(
    val customer : Customer = Customer(),
    val transactionHeads : List<TransactionHead> = emptyList()
)