package com.example.homebankfront.dataAccess.bodies


data class CustomerAndTransactionHeads(
    val customer : Customer = Customer(),
    val transactionHeads : List<TransactionHead> = emptyList()
)