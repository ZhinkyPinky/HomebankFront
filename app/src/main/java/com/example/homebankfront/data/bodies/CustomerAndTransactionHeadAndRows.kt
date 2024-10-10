package com.example.homebankfront.data.bodies

data class CustomerAndTransactionHeadAndRows(
    val customer : Customer = Customer(),
    val transactionHead : TransactionHead = TransactionHead(),
    val transactionRows : List<TransactionRow> = emptyList()
)
